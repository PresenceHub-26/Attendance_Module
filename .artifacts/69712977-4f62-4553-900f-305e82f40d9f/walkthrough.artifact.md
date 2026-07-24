# Walkthrough - Theme Toggle & Persistent Styling

I have added a Light/Dark mode toggle to the main menu and ensured the theme preference is saved and applied correctly across the entire app.

## Changes Made

### 1. Persistent Theme Management
- Updated `SessionManager` to store and retrieve the user's theme preference (`NightMode`).
- Updated `SplashActivity` to automatically apply the saved theme as soon as the app starts.

### 2. UI - Theme Toggle Menu
- Added a **Toggle Theme** option to the main toolbar menu (`menu_main.xml`).
- Implemented the toggle logic in both `DashboardActivity` and `MainActivity`.
- When the theme is toggled, the app immediately updates and saves the preference.

### 3. Icon & Menu Visibility
- Ensured all screens use the high-contrast `ToolbarTheme`. This makes the **three dots (overflow menu)** and other icons consistently bright white and easy to see on the primary blue background.
- Refined the layouts to use modern Material 3 styles for text input fields and buttons.

### 4. Cohesive Logic
- Unified the theme toggle behavior between the **Dashboard** and the **Reports** screens.

## Verification Results

### Automated Tests
- Ran `./gradlew app:assembleDebug`: **Build Successful**.

### Manual Verification (Instructions for User)
1. **Toggle Theme**:
    - Open the app and go to the **Dashboard**.
    - Click the **Compass icon** (or the overflow menu) in the top right toolbar.
    - Select **Toggle Theme**. The app will immediately switch between Light and Dark modes.
2. **Persistent State**:
    - Switch the app to **Dark Mode**.
    - Close the app completely and reopen it.
    - Verify that the app starts directly in **Dark Mode**.
3. **Visibility**:
    - Check the top right corner. The **three dots** (overflow menu) and any icons should be clearly visible and bright.
