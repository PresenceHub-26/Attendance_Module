# Implementation Plan - Data Sharing Relocation & Fix

Relocate the system data sharing tools to the Reports page and fix the sharing functionality to ensure seamless synchronization between devices.

## User Review Required

> [!IMPORTANT]
> - The **System Data Sharing** section (Export, Import, Share) will move from the "Manage Account" page to the **Reports** page.
> - I will be fixing the "Share Student List" button which was failing due to a security configuration mismatch (missing cache path in FileProvider).

## Proposed Changes

### [Component] Storage & Security

#### [MODIFY] [file_paths.xml](file:///C:/Users/Ohene/Downloads/Docs/GitHub/Attendance_Modul/app/src/main/res/xml/file_paths.xml)
- Add `<cache-path name="shared_exports" path="exports/" />` to allow the app to securely share temporary JSON export files.

### [Component] UI / Layouts

#### [MODIFY] [fragment_account.xml](file:///C:/Users/Ohene/Downloads/Docs/GitHub/Attendance_Modul/app/src/main/res/layout/fragment_account.xml)
- Remove the "System Data Sharing" section and its buttons.

#### [MODIFY] [fragment_report.xml](file:///C:/Users/Ohene/Downloads/Docs/GitHub/Attendance_Modul/app/src/main/res/layout/fragment_report.xml)
- Insert the "System Data Sharing" section above the "Filter Reports" header.
- Include buttons for **EXPORT**, **IMPORT**, and **SHARE STUDENT LIST**.

### [Component] Logic & Fragments

#### [MODIFY] [ReportFragment.java](file:///C:/Users/Ohene/Downloads/Docs/GitHub/Attendance_Modul/app/src/main/java/com/example/attendancemodule/fragments/ReportFragment.java)
- Move all sharing logic from `AccountFragment`.
- Upgrade to the modern **ActivityResultLauncher** API for handling file selection and saving (replacing the deprecated `startActivityForResult`).
- Fix the `shareStudentData` method to correctly generate a shareable URI via `FileProvider`.

#### [MODIFY] [AccountFragment.java](file:///C:/Users/Ohene/Downloads/Docs/GitHub/Attendance_Modul/app/src/main/java/com/example/attendancemodule/fragments/AccountFragment.java)
- Remove all methods and button listeners related to data sharing.

## Verification Plan

### Manual Verification
- **Functional Sharing**:
    - Tap **SHARE STUDENT LIST** in the Reports tab.
    - Verify that the Android Share sheet opens and allows sending the `.json` file to another app (e.g., WhatsApp).
- **Relocation Check**:
    - Verify the sharing tools are visible in the **Reports** tab.
    - Verify the sharing tools are no longer in the **Manage Account** tab.
- **Import/Export**:
    - Perform a full Export and Import cycle on the Reports page to ensure database integrity is maintained.
