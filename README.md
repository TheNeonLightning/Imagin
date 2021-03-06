# Imagin

<img src="/app/src/main/res/mipmap-hdpi/icon.png" align="left"
width="75" hspace="0" vspace="0"> 
Android application that sends your photo to the server  
and returns a processed photo and your colortype.
<br />
<br />
<br />

## About

The prototype of the application includes:
- Intro screens containing explanatory comments and hints
- Loading photo screen with the options to pick image from the storage or to use camera
- Intermediate processing result screen with a picture processed by the server
- Result screen showing calculated color type with the option to view other types

## Screenshots
When creating the application, the specified design was followed. 

[<img src="/screenshots/intro.jpg" align="left"
width="200"
    hspace="10" vspace="10">](/screenshots/intro.jpg)
    
[<img src="/screenshots/findimage.jpg" align="center"
width="200"
    hspace="10" vspace="10">](/screenshots/findimage.jpg)
    
<br />    
    
[<img src="/screenshots/sending.jpg" align="left"
width="200"
    hspace="10" vspace="10">](/screenshots/sending.jpg)
    
[<img src="/screenshots/result.jpg" align="center"
width="200"
    hspace="10" vspace="10">](/screenshots/result.jpg)

## Permissions and requirements
Minimal SDK version - 21  
Imagin requires the following permissions (will request on version Android 6.0 and higher):
- Read and write access to external storage
- Access camera to take a photo  

Application also uses Internet connection to send photo to the server

## Some application details
Using [AppIntro2](https://github.com/AppIntro/AppIntro) library for implementing the intro screens. 
Also working with Android persistent storage: in order to show the intro only on the first launch.  

For interaction with the server using the [Retrofit 2](https://square.github.io/retrofit/) library. 
Before sending the image is converted to base64 representation. Image from response is saved in the storage.  

Authentication with JsonWebToken (JWT), for this implemented:
- AuthInterceptor - intercepts all requests and adds the required token to the header
- TokenAuthenticator - an object that intercepts 401 status from the server (Unauthorized), 
  attempts to refresh the token and repeats the failed request with a new token
- Storing JWT token in shared preferences








