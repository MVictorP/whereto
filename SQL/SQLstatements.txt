
/* begin table creation */

DROP TABLE IF EXISTS attendance_statuses;
CREATE TABLE attendance_statuses (
  Attendance_Status_ID INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  Attendance_Status_Name VARCHAR(50) NOT NULL,
  PRIMARY KEY (Attendance_Status_ID),
  UNIQUE INDEX Attendance_Status_ID (Attendance_Status_ID)
);


DROP TABLE IF EXISTS employee;
CREATE TABLE employee (
  Employee_ID INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  UsrFirst_Name VARCHAR(50) DEFAULT 'probation',
  UsrLast_Name VARCHAR(50) DEFAULT 'probation',
  email VARCHAR(50) NOT NULL,
  password VARCHAR(32) NOT NULL,
  PRIMARY KEY (Employee_ID),
  UNIQUE INDEX email (email),
  UNIQUE INDEX Employee_ID (Employee_ID)
);

DROP TABLE IF EXISTS meeting_departments;
CREATE TABLE meeting_departments (
  Meeting_Dept_ID INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  Meeting_Dept_Name VARCHAR(50) NOT NULL,
  PRIMARY KEY (Meeting_Dept_ID),
  UNIQUE INDEX Meeting_Status_ID (Meeting_Dept_ID)
);

DROP TABLE IF EXISTS response_types;
CREATE TABLE response_types (
  Response_Type_ID INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  Response_Name VARCHAR(50) NOT NULL,
  PRIMARY KEY (Response_Type_ID),
  UNIQUE INDEX Response_Type_ID (Response_Type_ID)
);

DROP TABLE IF EXISTS rooms;
CREATE TABLE rooms (
  Room_ID INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  Capacity INT(10) UNSIGNED DEFAULT NULL,
  Room_Num VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (Room_ID),
  UNIQUE INDEX Room_ID (Room_ID)
);

DROP TABLE IF EXISTS meetings;
CREATE TABLE meetings (
  Meeting_ID INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  Meeting_Name VARCHAR(255) NOT NULL,
  Meeting_Description VARCHAR(255) NOT NULL,
  Meeting_DateTime DATETIME NOT NULL,
  Meeting_Duration INT(10) UNSIGNED NOT NULL,
  Meeting_Dept_ID INT(10) UNSIGNED NOT NULL,
  Meeting_Room_ID INT(10) UNSIGNED NOT NULL,
  Organizer_ID INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (Meeting_ID),
  INDEX FK_meetings_employee_Employee_ID (Organizer_ID),
  UNIQUE INDEX Meeting_ID (Meeting_ID),
  CONSTRAINT FK_meetings_meeting_departments_Meeting_Dept_ID FOREIGN KEY (Meeting_Dept_ID)
    REFERENCES meeting_departments(Meeting_Dept_ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_meetings_rooms_Room_ID FOREIGN KEY (Meeting_Room_ID)
    REFERENCES rooms(Room_ID) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS attendance;
CREATE TABLE attendance (
  Attendance_ID INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  Employee_ID INT(10) UNSIGNED DEFAULT NULL,
  Meeting_ID INT(10) UNSIGNED NOT NULL,
  Response_DateTime DATETIME DEFAULT NULL,
  Response_Type_ID INT(10) UNSIGNED DEFAULT NULL,
  Attendance_Status_ID INT(10) UNSIGNED DEFAULT NULL,
  PRIMARY KEY (Attendance_ID),
  UNIQUE INDEX Attendance_ID (Attendance_ID),
  CONSTRAINT FK_attendance_attendance_statuses_Attendance_Status_ID FOREIGN KEY (Attendance_Status_ID)
    REFERENCES attendance_statuses(Attendance_Status_ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_attendance_employee_Employee_ID FOREIGN KEY (Employee_ID)
    REFERENCES employee(Employee_ID) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT FK_attendance_meetings_Meeting_ID FOREIGN KEY (Meeting_ID)
    REFERENCES meetings(Meeting_ID) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT FK_attendance_response_types_Response_Type_ID FOREIGN KEY (Response_Type_ID)
    REFERENCES response_types(Response_Type_ID) ON DELETE NO ACTION ON UPDATE NO ACTION
);

/* end table creation */

/* begin data population */

/* attendance status data */
INSERT INTO attendance_statuses VALUES
(1, 'absent'),
(2, 'early'),
(3, 'late'),
(4, 'on-time'),
(5, 'unchecked');

/* employee data */
INSERT INTO employee VALUES
(1, 'Deborah', 'Mitchell', 'deb@aol.com', 'pass'),
(2, 'Jeremy', 'Stevens', 'jer@aol.com', 'pass'),
(3, 'Judy', 'Bishop', 'jbishop2@admin.ch', 'OYjP6P7'),
(4, 'Sara', 'Reyes', 'sreyes3@zimbio.com', 'g4FcBCBiE1'),
(5, 'Doris', 'Perry', 'dperry4@artisteer.com', 'YWbcfZc8'),
(6, 'Tammy', 'Ellis', 'tellis5@huffingtonpost.com', 'oVPchBAk2rd3'),
(7, 'Harold', 'Knight', 'hknight6@blog.com', 'kvDpS5VjU'),
(8, 'Bonnie', 'Roberts', 'broberts7@google.fr', '1VYlGUCSeK'),
(9, 'Henry', 'Stephens', 'hstephens8@ovh.net', 'baNKf4T'),
(10, 'Marie', 'Mccoy', 'mmccoy9@360.cn', 'QNnZISEb'),
(11, 'Pamela', 'Coleman', 'pcolemana@wp.com', 'DpTQgNwaecOx'),
(12, 'Justin', 'Thompson', 'jthompsonb@wordpress.com', 'ejX7so87R3TQ'),
(13, 'Joyce', 'Hughes', 'jhughesc@sitemeter.com', 'x1jUvG7IR'),
(14, 'Harold', 'Frazier', 'hfrazierd@aol.com', '57uHNKcK'),
(15, 'Daniel', 'Lane', 'dlanee@vimeo.com', 'D5CrGaonxi'),
(16, 'Roy', 'Tucker', 'rtuckerf@eventbrite.com', 'me6Z25d'),
(17, 'Justin', 'Ferguson', 'jfergusong@twitter.com', 'SI5N37auS'),
(18, 'Harry', 'Simpson', 'hsimpsonh@vistaprint.com', 'gecu3q3fu'),
(19, 'Fred', 'Martinez', 'fmartinezi@mit.edu', 'zqQMkA'),
(20, 'Robin', 'Rivera', 'rriveraj@wp.com', 'Dfdp1V'),
(21, 'Julie', 'Simpson', 'jsimpsonk@bigcartel.com', 'UKU2PI'),
(22, 'Catherine', 'Moore', 'cmoorel@microsoft.com', 'wcCOfFgD'),
(23, 'Daniel', 'George', 'dgeorgem@microsoft.com', 'LVC2p2C'),
(24, 'Robin', 'Martinez', 'rmartinezn@last.fm', '9NcJMxkO'),
(25, 'Amy', 'Coleman', 'acolemano@w3.org', 'u75T4S'),
(26, 'Jonathan', 'Holmes', 'jholmesp@paypal.com', 'HweJ7y378U'),
(27, 'Doris', 'Mills', 'dmillsq@xing.com', 'apbTv9z8bia'),
(28, 'Lori', 'Carroll', 'lcarrollr@indiegogo.com', 'fPOdUqfgPZ'),
(29, 'Shirley', 'Fox', 'sfoxs@google.fr', 'Z1Y30Ch'),
(30, 'Stephanie', 'Oliver', 'solivert@baidu.com', 'YU1ikpW7m'),
(31, 'Anna', 'Matthews', 'amatthewsu@statcounter.com', 'vI0jxQi5'),
(32, 'Denise', 'Castillo', 'dcastillov@printfriendly.com', 'StKadr'),
(33, 'Betty', 'Gomez', 'bgomezw@abc.net.au', 'EVYmJFHod'),
(34, 'Raymond', 'Butler', 'rbutlerx@eepurl.com', 'S52QY89Vy49z'),
(35, 'Barbara', 'Sims', 'bsimsy@csmonitor.com', '0S1ABC13'),
(36, 'Sandra', 'Fields', 'sfieldsz@gizmodo.com', 'XgAK2WU128M'),
(37, 'Richard', 'Black', 'rblack10@phpbb.com', 'xV19jJ'),
(38, 'Jack', 'Anderson', 'janderson11@dion.ne.jp', 'URPuDYi9l5R'),
(39, 'Richard', 'West', 'rwest12@sun.com', 'DwCZBI66E'),
(40, 'Tammy', 'Reed', 'treed13@linkedin.com', 'GpB5scbGxaX8'),
(41, 'Susan', 'Gonzalez', 'sgonzalez14@woothemes.com', 'uzuWGvd'),
(42, 'Patrick', 'Dean', 'pdean15@ifeng.com', 'RGEoE6j'),
(43, 'Diana', 'Gutierrez', 'dgutierrez16@jimdo.com', '1M4hxS4p1W'),
(44, 'Denise', 'Greene', 'dgreene17@sfgate.com', 'ICjkyKJfd'),
(45, 'Michelle', 'Peters', 'mpeters18@chron.com', 'OGJmkOUzV'),
(46, 'Robin', 'Banks', 'rbanks19@cbsnews.com', '6EYDDdqFip'),
(47, 'Ashley', 'Freeman', 'afreeman1a@sbwire.com', 'V21AJ7c2'),
(48, 'Charles', 'Garcia', 'cgarcia1b@un.org', 'mcF15RY2qD'),
(49, 'Shawn', 'Payne', 'payne@aol.com', 'pass'),
(50, 'Robin', 'Stevens', 'rstevens1d@mashable.com', 'h6x2py');

/* meeting department data */
INSERT INTO meeting_departments VALUES
(1, 'Administration'),
(2, 'Accounting'),
(3, 'Marketing'),
(4, 'Production'),
(5, 'Sales'),
(6, 'Customer Service'),
(7, 'IT'),
(8, 'All departments');

/* response type data */
INSERT INTO response_types VALUES
(1, 'Accept'),
(2, 'Decline');

/* room data */
INSERT INTO rooms VALUES
(1, 26, '110'),
(2, 41, '109'),
(3, 23, '108'),
(4, 20, '107'),
(5, 23, '116'),
(6, 35, '106'),
(7, 30, '115'),
(8, 50, '113'),
(9, 14, '104'),
(10, 37, '103'),
(11, 41, '102'),
(12, 20, '112'),
(13, 12, '111');

/* end data population */