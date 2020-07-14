import json
import urllib.request
from urllib.error import URLError
import mysql.connector
import time
import http.client
import signal
import keyring


def handler(signum, frame):
    raise TimeoutError


insert_string = """INSERT INTO market_data_dump (ItemID, IconURL, ItemName) VALUES (%s, %s, %s)"""
x = 0
signal.signal(signal.SIGALRM, handler)
# download raw json object
while x < 60000:
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
        pw = keyring.get_password('SQL_RS3', 'zjohnso')
        cnx = mysql.connector.connect(user='root', password=pw,
                                      host='localhost',
                                      database='RS3DataHouse')
        cursor = cnx.cursor()
        cursor.execute(insert_string, fields)
        cnx.commit()
        cnx.close()
        print('Success')
        x += 1
    except URLError:
        print('Item is not on the market: ' + str(item_id))
        x += 1
    except mysql.connector.errors.IntegrityError:
        print('Item already exists in table: ' + str(item_id))
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
