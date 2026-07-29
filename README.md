# Student Attendance Pro

A professional, high-performance, and offline-capable Student Attendance Management System built for Android. This application follows modern architectural standards and provides a premium user experience for educational institutions.

## 🚀 Key Features

### 🔐 Advanced User Management
- **Super Admin Role**: Hierarchical control allowing the primary administrator to manage (add/delete) other staff/admin accounts.
- **Secure Authentication**: Validated login with session management and identity-verified password updates.
- **Account Settings**: Dedicated profile management for all users to update credentials securely.

### 👥 Student Directory
- **Full CRUD Support**: Add, view, edit, and remove student records with ease.
- **Detailed Profiles**: Store full names, student IDs, departments, levels, and contact information.
- **Smart Search**: Instantly find students using a modern pill-shaped search bar with name or ID filtering.

### 📝 Efficient Attendance Marking
- **High-Speed Workflow**: Persistent bottom navigation and a centered FAB for instant access.
- **Smart Filters**: Quickly narrow down student lists by **Department** or **Level** using modern filter chips.
- **Visual Feedback**: Vibrant, color-coded status indicators (Green for Present, Red for Absent).

### 📊 Advanced Reporting & Analytics
- **System Analytics**: Real-time dashboard with dynamic statistics (Total, Present, Absent, and Attendance Rate).
- **History Logs**: Detailed chronological logs with dual-search and professional date pickers.
- **Professional PDF Export**: Generate A4-ready attendance reports with dedicated Date and Time columns.
- **Integrated Sharing**: Share PDF reports instantly via WhatsApp, Email, or Print using the Android Share Sheet.

### 📤 Data Portability & Sharing
- **System Data Sharing**: Export the entire student database to JSON for backup.
- **Instant Synchronization**: Direct "Share Student List" feature to sync databases between different staff devices without a server.

## 🎨 UI/UX Highlights
- **Single Activity Architecture**: Modern fragment-based navigation for a fluid, zero-flicker experience.
- **Material Design 3**: Fully compliant with M3 standards, featuring rounded cards, elevated surfaces, and refined typography.
- **Slate & Blue Palette**: A custom, eye-friendly "Cool Slate" background with vibrant Royal Blue accents.
- **Adaptive Dark Mode**: A premium "Deep Midnight" dark theme optimized for low-light environment visibility.
- **Pill UI Components**: Consistent use of modern rounded search bars and input fields.

## 🛠️ Technical Stack
- **Language**: Java
- **Architecture**: Single Activity + Fragments
- **Database**: SQLite (Version 2) with relational integrity
- **UI Components**: Material 3, CoordinatorLayout, BottomAppBar, ChipGroups
- **Utilities**: Custom PDF Generation, JSON Serialization, FileProvider Security

## 📂 Project Structure
- `activities/`: Hosting activities and authentication flow.
- `fragments/`: Core modularized screens (Dashboard, Students, Mark, Reports).
- `adapters/`: High-performance RecyclerView adapters for students, admins, and reports.
- `database/`: Centralized SQLite logic and JSON data handling.
- `models/`: Plain Java Objects (POJOs) for Students, Users, and Attendance.
- `utils/`: Reusable helpers for Date/Time, PDF Generation, and Session Management.

## ⚙️ Setup & Installation
1. Clone the repository to your local machine.
2. Open the project in **Android Studio**.
3. Sync the project with Gradle files.
4. Run the app on an emulator or physical device (Target API 31+).

**Default Super Admin Credentials:**
- **Username**: `admin`
- **Password**: `admin123`

---
*Developed with focus on efficiency, visibility, and professional administration.*
