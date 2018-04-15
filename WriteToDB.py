import pymysql
import serial

port = serial.Serial("/dev/ttyUSB0", baudrate=10412, timeout=None)
# Open database connection
db = pymysql.connect(host='mydbinstance.cbooiucez8xp.eu-west-1.rds.amazonaws.com',user='kieransDatabase',passwd='thisismypassword',db='TESTDB')

# prepare a cursor object using cursor() method
cursor = db.cursor()

while True:
    rcv=port.readline()
    words=rcv.split(" ")
    print(words[1])
    print(words[4])
    ms=int(words[1])
    us=int(words[4])
    ans=(ms*1.024)+(us*.0039)
    #print("{0:.2f}".format(ans))
    trip=str("{0:.2f}".format(ans))

# Prepare SQL query to INSERT a record into the database.
    sql = "INSERT INTO testResults(unitNum, \
    tripLevel, tripTime, result) \
    VALUES ('%s', '%s', '%s', '%s' )" % \
    ('2874', '30',trip,'PASS')
    try:
       # Execute the SQL command
       cursor.execute(sql)
       # Commit your changes in the database
       db.commit()
    except:
       # Rollback in case there is any error
       db.rollback()
       # disconnect from server
       db.close()
