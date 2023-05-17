# Student Management System

### Student Attendance Logging System using NFC Cards 

<br>
<br>

## **Overview**
-----
The **Student Management System** is a web-based application that uses MySQL database and Spring Boot backend to track and manage attendance data for students in a school or educational institution. The system uses NFC card technology to log attendance data and is powered by custom-made software running on an Arduino board, which provides audio and visual feedback to users during the logging process.

The system has three roles: superadmin, admin, and student. The superadmin role has full access to the system and can view, edit, and add user data, workgroups, and schedules. The admin role is assigned to teachers who have access to view attendance data for students in workgroups, manage student data, and edit attendance data if necessary. The student role is assigned to individual students who can view their own attendance data and schedule information.

The system is designed to be user-friendly and efficient, with features that allow for quick and easy data entry and management. The NFC logging system is an essential component of the attendance tracking process, providing an accurate and reliable method for tracking student attendance. The system also supports the use of a master card, which can be used to register additional NFC cards to the system.

The student attendance logging system is a valuable tool for educational institutions, providing a centralized and efficient method for tracking student attendance and managing attendance data. With its user-friendly interface and advanced features, the system helps to ensure that attendance data is accurately collected and stored, and that students and teachers can easily access the information they need to manage their schedules and attendance.

- Superadmin
- Admin (Teacher)
- Student
---
## User Accounts
The application provides three types of user accounts (for testing purpuses only), each with a predefined username and password:

1. Superadmin:
   - Username: superadmin
   - Password: superadmin

2. Admin:
   - Username: admin
   - Password: admin

3. Student:
   - Username: student
   - Password: student

---
## Superadmin

The superadmin role has full access to the user data and can edit it or add new users, workgroups, and schedules. The superadmin can perform the following actions:

- View all user data: The superadmin can view all user data in the system, including personal information, attendance data, workgroup assignments, and more.
- Edit user data: The superadmin can edit user account information, including personal information, workgroup assignments, and attendance data.
- Add new users: The superadmin can add new users to the system, including student and admin accounts. This includes assigning workgroups, schedules, and other relevant information.
- View all workgroups: The superadmin can view all workgroups in the system and associated data, including schedules and assigned users.
- Add new workgroups: The superadmin can add new workgroups to the system, including schedules and assigned users.
- View all schedules: The superadmin can view all schedules in the system, including associated workgroups and users.
- Create new schedules: The superadmin can create new schedules for workgroups, including defining class times and dates.
- Assign NFC cards: The superadmin can assign an NFC card to a user, which the user can then use to log their attendance.
- Delete users: The superadmin can delete user accounts from the system. When a user account is deleted, all associated data (including attendance data) is **not permanently** deleted.
- Restore deleted users: The superadmin can restore deleted user accounts within a specified time frame.
- Set user role: The superadmin can assign different roles to users.
- Delete NFC cards: The superadmin can delete NFC cards from the system.
- Get all required user data: The superadmin can view all user data in the system, including attendance data, workgroup assignments, and personal information.

The superadmin role is critical to the system's functionality, as it allows for the management of user data and the creation and management of workgroups and schedules. With this level of access, the superadmin can ensure that the system is functioning correctly and that all necessary data is being collected and stored accurately.

---

## Admin (Teacher)

The admin role is assigned to teachers who have limited access to the system. Each admin can view the attendance data of the students in their specific workgroups. An admin can be assigned to multiple workgroups, depending on their teaching responsibilities.

 The admin can perform the following actions:

- View attendance data of students in their specific workgroup: The admin can view attendance data of students assigned to their specific workgroup. This includes dates and times of attendance, as well as any notes or comments associated with the attendance record.
- Edit user attendance if wrongly logged: If an attendance record is wrongly logged, the admin can edit the record to correct any mistakes or errors.
- View user data with limitations: The admin can view user data of students assigned to their specific workgroup.
- Manage students in the specific workgroups: The admin can manage students assigned to their specific workgroup. This includes assigning new students to the workgroup, removing students from the workgroup, and updating student attendance as needed.

The admin role is essential to the system's functionality, as it allows for the management of attendance data and the monitoring of student attendance for specific workgroups. With this level of access, the admin can ensure that attendance data is accurately collected and stored for students in their specific workgroup.

---

## Student

The student role can only view their own attendance data. The student cannot view or edit any other user data. 

The student can perform the following actions:

- View their own attendance data: The student can view their own attendance data in the system, including dates and times of attendance.
- View their own schedule: The student can view their own schedule in the system, including dates and times of classes or other activities associated with their workgroup.

The student role is essential to the system's functionality, as it allows individual students to monitor their own attendance and schedule information. With this level of access, the student can ensure that they are attending classes and activities as required and can stay informed about their workgroup's schedule.

---
## Workgroups

Each student belongs to a workgroup. Workgroups have their own schedules. The superadmin can create new workgroups and schedules. The admin and student roles can only view the schedules of their specific workgroup.

---
## Attendance Data

The attendance data is stored in a database. The data includes the student's name, workgroup, schedule, and attendance status.

---

## User Accounts

Each user has their own account and personal information. The account information includes: 

- user's name
- email address
- password
- birth
- phone number

The personal information includes the user's workgroup, schedule, and attendance data.

---

## Deletion of User Accounts

When a user account is deleted from the system by the superadmin, it is not permanently deleted from the database. Instead, the delete action is only logical, and all associated data (including attendance data) remains in the system. This means that if a user account is restored within a specified time frame, all associated data will be restored as well. The superadmin can also view and manage all deleted user accounts and associated data.

This approach to deleting user accounts ensures that the system can maintain accurate records while also providing the flexibility to manage user accounts as needed.

---


## NFC Logging System

The attendance logging system uses NFC card technology to log the attendance of students. The system is powered by custom-made software running on an Arduino board, which provides audio and visual feedback when a user logs their attendance. If there is an error during the logging process, the system also provides audio and visual feedback to alert the user.

The NFC logging system is an essential component of the attendance tracking process, as it allows for accurate and efficient tracking of student attendance. With this technology, users can simply tap their NFC card to the reader to log their attendance, reducing the need for manual data entry and ensuring that attendance data is accurately collected and stored in the system.

---

## NFC Cards


Each student is assigned an NFC card. The NFC card is used to log the student's attendance. When the student taps their card, the system records their attendance status.

---


## Master Card

The system includes a master card which can be used to register additional NFC cards to the system. To add a new NFC card, the following steps should be taken:

1. Scan the master card to access the card registration mode.
2. Scan the new NFC card to register it to the system.
3. Repeat step 2 for any additional NFC cards that need to be registered.
4. Once all new cards have been registered, scan the master card again to exit card registration mode.

---

## Conclusion

<br>

Overall, the student attendance logging system provides an efficient and effective way to track student attendance in different workgroups. By using NFC cards and assigning different roles to different users, the system is designed to be user-friendly and easy to use. With this documentation, users should be able to navigate the system with ease and take full advantage of its features.


