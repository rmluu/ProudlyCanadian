# Proudly Canadian

**Proudly Canadian** is a Kotlin Android application built using Jetpack Compose, Firebase, and ML Kit. 
- It allows users to scan product barcodes, retrieve product information, and organize scanned items into personalized collections. 
- The goal is to help users discover and support Canadian-made products.

---

## Features

- User authentication with Google via Firebase Authentication
- Barcode scanning using Google ML Kit and CameraX
- Product data fetched using GS1 and UPCItemDB APIs
- Add scanned products to custom user-defined lists
- View, manage, and delete products from lists
- Firebase Firestore integration for real-time data sync per user

---

## Tech Stack

- Kotlin and Jetpack Compose for UI
- Firebase Authentication and Firestore for backend
- ML Kit Barcode Scanning API
- Retrofit for API requests (GS1 and UPCItemDB)
- Coil for image loading

---

## App Structure

```
com.example.proudlycanadian
│
├── ui/                   # Compose UI components and screens
│   └── screens/          # Individual screen composables (Scan, Lists, etc.)
│
├── api/                  # API models and data fetching logic
│   ├── model/            # FirestoreProduct, Collection
│   └── ProductManager.kt # API integration logic
│
├── viewmodel/            # ViewModels for managing state
│   └── ListViewModel.kt
│
├── navigation/           # Navigation graph and routes
└── MainActivity.kt       # App entry point
```

---

## Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new Firebase project and register your Android app
3. Download the `google-services.json` file and place it in the `app/` directory
4. Enable "Email/Password" and "Google" as sign-in methods under Firebase Authentication
5. Enable Firestore Database (start in test mode for development)

---

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/proudly-canadian.git
   cd proudly-canadian
   ```
2. Open the project in Android Studio
3. Make sure your Firebase project is correctly linked and all dependencies are synced
4. Run the app on an emulator or physical device with camera and internet access

---

## Requirements

- Android Studio Giraffe or newer
- Minimum SDK: 24
- Target SDK: 35
- Internet access (for API calls and Firebase sync)
- Camera access (for barcode scanning)

---

## Author

Richard Luu
Business Information Technology student at Red River College Polytechnic  
