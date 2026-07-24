# Implementation Plan - Dashboard Module

Introduce a central Dashboard as the landing page after login, featuring a modern UI with summary cards and a Navigation Drawer for app-wide navigation.

## Proposed Changes

### [Component] UI & Layouts

#### [NEW] [activity_dashboard.xml](file:///C:/Users/Ohene/Downloads/Docs/GitHub/Attendance_Modul/app/src/main/res/layout/activity_dashboard.xml)
- `DrawerLayout` as the root.
- `CoordinatorLayout` for the main content.
- `AppBarLayout` + `MaterialToolbar`.
- Main content area with summary cards (Total Students, Present, Absent, %).
- `NavigationView` for the side drawer.

#### [NEW] [nav_header.xml](file:///C:/Users/Ohene/Downloads/Docs/GitHub/Attendance_Modul/app/src/main/res/layout/nav_header.xml)
- Header for the navigation drawer showing the app logo and "Student Attendance System".

#### [NEW] [drawer_menu.xml](file:///C:/Users/Ohene/Downloads/Docs/GitHub/Attendance_Modul/app/src/main/res/menu/drawer_menu.xml)
- Menu items for:
    - Dashboard (Home)
    - Student List
    - Attendance
    - Reports
    - Logout

### [Component] Logic & Activities

#### [NEW] [DashboardActivity.java](file:///C:/Users/Ohene/Downloads/Docs/GitHub/Attendance_Modul/app/src/main/java/com/example/attendancemodule/activities/DashboardActivity.java)
- Set up `ActionBarDrawerToggle`.
- Handle navigation drawer item clicks.
- Fetch summary statistics from `DatabaseHelper` and update cards.
- Implement session check (redirect to Login if needed).

#### [NEW] [StudentListActivity.java](file:///C:/Users/Ohene/Downloads/Docs/GitHub/Attendance_Modul/app/src/main/java/com/example/attendancemodule/activities/StudentListActivity.java)
- A simple activity to view all students (can be expanded later with add/edit/delete).
- Will use a basic RecyclerView.

#### [MODIFY] [SplashActivity.java](file:///C:/Users/Ohene/Downloads/Docs/GitHub/Attendance_Modul/app/src/main/java/com/example/attendancemodule/activities/SplashActivity.java) & [LoginActivity.java](file:///C:/Users/Ohene/Downloads/Docs/GitHub/Attendance_Modul/app/src/main/java/com/example/attendancemodule/activities/LoginActivity.java)
- Change navigation target from `MainActivity` to `DashboardActivity` on successful login/session check.

### [Component] Manifest & Navigation

#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/Ohene/Downloads/Docs/GitHub/Attendance_Modul/app/src/main/AndroidManifest.xml)
- Register `DashboardActivity` and `StudentListActivity`.

## Verification Plan

### Manual Verification
- Log in and verify redirection to the new **Dashboard**.
- Check that the summary cards display correct, real-time data from the database.
- Open the **Navigation Drawer** and verify:
    - Icons and text are modern and visible.
    - Clicking **Attendance** takes you to the marking screen.
    - Clicking **Reports** takes you to the reports screen.
    - Clicking **Logout** clears the session and takes you to Login.
- Verify that the Dashboard automatically updates if you return to it after marking attendance.
