# Project Reset to Empty Activity

Reset the project to a state equivalent to a fresh "Empty Views Activity" project, removing all components created for the Attendance Module.

## Proposed Changes

### 1. Java Code Cleanup

#### [MODIFY] [MainActivity.java](file:///C:/Users/Ohene/Downloads/Docs/GitHub/Attendance_Modul/app/src/main/java/com/example/attendancemodule/MainActivity.java)
- Update `setContentView` to use `R.layout.activity_main`.
- Remove any lingering custom logic.

#### [DELETE] Redundant Packages and Classes
- Remove `adapters`, `constants`, `database`, `models`, and `utils` packages.
- Delete all activities except `MainActivity.java`:
    - `AddStudentActivity.java`
    - `AttendanceActivity.java`
    - `DashboardActivity.java`
    - `EditStudentActivity.java`
    - `LoginActivity.java`
    - `ReportsActivity.java`
    - `SplashActivity.java`
    - `StudentListActivity.java`

---

### 2. Resource Cleanup

#### [DELETE] Redundant Layouts
- Remove all layouts except `activity_main.xml`:
    - `activity_add_student.xml`
    - `activity_attendance.xml`
    - `activity_dashboard.xml`
    - `activity_login.xml`
    - `activity_reports.xml`
    - `activity_splash.xml`
    - `activity_student_list.xml`
    - `item_report.xml`
    - `item_student.xml`
    - `item_student_attendance.xml`

#### [DELETE] Redundant Resource Directories
- Remove `app/src/main/res/menu/`.

#### [MODIFY] [strings.xml](file:///C:/Users/Ohene/Downloads/Docs/GitHub/Attendance_Modul/app/src/main/res/values/strings.xml)
- Remove `navigation_drawer_open` and `navigation_drawer_close`.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to ensure the project builds correctly in its reset state.

### Manual Verification
- Verify in the project explorer that only `MainActivity` and `activity_main.xml` remain as the primary UI components.
