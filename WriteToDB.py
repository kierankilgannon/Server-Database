

import pymysql
import serial

port = serial.Serial("/dev/ttyUSB0", baudrate=10412, timeout=15)
# Open database connection
db = pymysql.connect(host='mydbinstance.cbooiucez8xp.eu-west-1.rds.amazonaws.com',user='kieransDatabase',passwd='thisismypassword',db='TESTDB')

# prepare a cursor object using cursor() method
cursor = db.cursor()

# Drop table if it already exist using execute() method.
cursor.execute("DROP TABLE IF EXISTS EMPLOYEE")

# Create table as per requirement
sql = """CREATE TABLE EMPLOYEE (
   FIRST_NAME  CHAR(20) NOT NULL,
   LAST_NAME  CHAR(20),
   AGE INT,  
   SEX CHAR(1),
   INCOME FLOAT )"""

cursor.execute(sql)
while True:
    rcv = port.readline()
    print(rcv)

# Prepare SQL query to INSERT a record into the database.
    sql = "INSERT INTO EMPLOYEE(FIRST_NAME, \
       LAST_NAME, AGE, SEX, INCOME) \
       VALUES ('%s', '%s', '%d', '%c', '%d' )" % \
       ('Mac', 'Mohan', 20, 'M', int(rcv))
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