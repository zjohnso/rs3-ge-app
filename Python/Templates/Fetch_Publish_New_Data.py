import json
import urllib.request
from urllib.error import URLError
import mysql.connector
import time
import http.client
import signal
import keyring
import collections
from github import Github
from github import GithubException


def handler(signum, frame):
    raise TimeoutError


pw = keyring.get_password('SQL_RS3', 'zjohnso')
cnx = mysql.connector.connect(user='root', password=pw,
                              host='localhost',
                              database='RS3DataHouse')
cursor = cnx.cursor()
cursor.execute("SELECT * FROM RS3DataHouse.market_data_dump")
records = cursor.fetchall()
row_count = len(records)
last_id = records[row_count - 1][0]
cnx.close()

insert_string = """INSERT INTO market_data_dump (ItemID, IconURL, ItemName) VALUES (%s, %s, %s)"""
x = last_id + 1
gap = 0
signal.signal(signal.SIGALRM, handler)
# download raw json object
while gap < 5000:
    base_url = "https://secure.runescape.com/m=itemdb_rs/api/catalogue/detail.json?item="
    item_id = x
    url = base_url + str(item_id)
    try:
        signal.alarm(60)
        data = urllib.request.urlopen(url).read().decode("ISO-8859-1")
        signal.alarm(0)
        # parse the object
        obj = json.loads(data)
        # output some object attributes
        icon = obj['item']['icon']
        name = obj['item']['name']
        fields = (item_id, icon, name)
        print('Adding row to table:', item_id, icon, name)
        cnx = mysql.connector.connect(user='root', password=pw,
                                      host='localhost',
                                      database='RS3DataHouse')
        cursor = cnx.cursor()
        cursor.execute(insert_string, fields)
        cnx.commit()
        cnx.close()
        print('Success')
        gap = 0
        x += 1
    except URLError:
        print('Item is not on the market: ' + str(item_id))
        gap += 1
        x += 1
    except mysql.connector.errors.IntegrityError:
        print('Item already exists in table: ' + str(item_id))
        gap = 0
        x += 1
    except json.decoder.JSONDecodeError:
        print('DDoS error at item: ' + str(item_id))
        print('Retrying in 30 seconds...')
        time.sleep(30)
    except http.client.RemoteDisconnected:
        print('Server disconnected at item: ' + str(item_id))
        print('Retrying in 30 seconds...')
        time.sleep(30)
    except TimeoutError:
        print('Timeout error at item: ' + str(item_id))
        print('Retrying...')

cnx = mysql.connector.connect(user='root', password=pw,
                              host='localhost',
                              database='RS3DataHouse')
cursor = cnx.cursor()
cursor.execute("SELECT * FROM RS3DataHouse.market_data_dump")
records = cursor.fetchall()

objects = []
for row in records:
    d = collections.OrderedDict()
    d['id'] = row[0]
    d['name'] = row[2]
    objects.append(d)

j = json.dumps(objects, indent=2)
objects_file = 'RS3_Items.json'
f = open(objects_file, 'w')
print(j, file=f)

cnx.close()

token = keyring.get_password('GitHub', 'zjohnso')

# authenticate to github
g = Github(login_or_token=token)
# get the authenticated user
user = g.get_user()
repo = g.search_repositories("rs3-ge-app")[0]
f = open('RS3_Items.json', 'r')
try:
    contents = repo.get_contents('Python/RS3_Items.json')
    repo.update_file('Python/RS3_Items.json', 'Updating JSON of item database', f.read(), contents.sha)
except GithubException:
    repo.create_file('Python/RS3_Items.json', 'Creating JSON of item database', f.read())
f.close()
