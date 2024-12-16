# Project Overview

The **HealthCare** App is a simple Android application built using Kotlin and Jetpack Compose. It demonstrates an MVVM architecture with essential features such as user login, data display from a mock API, and navigation between screens. The app is designed as part of an assessment to showcase modern Android development skills.

## Key Features
- Login Screen: Users can log in by entering a username (no validation required).
- Greeting: Displays a greeting message personalized with the user's name and the time of day.
- Medicine List: Fetches and displays a list of medicines from a mock API in card format.
- Medicine Details: Tapping a medicine card navigates to a detailed view showing its name, dose, and strength.
- Offline Storage: Uses Room Database to store medicine data for offline access.
- Error Handling: Displays appropriate UI for loading, success, and failure states.

## Tech Stack

- Language: Kotlin
- UI Framework: Jetpack Compose
- Clean Architecture: MVVM
- Dependency Injection: Hilt
- Navigation: Jetpack Navigation with NavGraph
- Database: Room
- Networking: Retrofit and Mocky.io for mock API integration
- State Management: StateFlow
- Testing: JUnit and Mockito
- Setup Instructions

**Clone the repository:**
git clone https://github.com/dev-habib-rehman/HealthCare

Open the project in Android Studio (latest version recommended).
Sync the project to download all dependencies.
Build and run the app on an emulator or a physical device.

**Usage**
Launch the app.
Enter a username on the login screen and proceed.
View the greeting message and the list of medicines.
Tap on a medicine card to view its details.

**API Details**
https://run.mocky.io/v3/cd4291d3-1ff3-4a38-8fe2-dbf36008ca27
The app uses a mock API hosted on Mocky.io to fetch medicine data. Below is the JSON structure:

{
"medicines": [
{
"id": 1,
"name": "Paracetamol",
"dose": "500mg",
"strength": "Moderate"
},
... (additional entries)
]
}

-**Unit Testing**
- clone the project navigate to test directory run the classes
- I have added multiple test cases for AppRepository to test communication data.
- I have added multiple test cases for UI layer ViewModel to test the data flow.
- By testing above two major classes almost other main classes also get tested.

-**Instrument Testing**
- Added Instrument for testing database operations like insert and retrieve. 
- setup the project along with emulator or real device. 
- got the AndroidTest directory and run the AppDaoTest Class. 


Feel free to reach out for questions or issues. Enjoy exploring the app!