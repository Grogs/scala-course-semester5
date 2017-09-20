# Lesson 8

This lesson is mostly a presentation and code walkthrough. 
The slides are here: http://grogs.github.io/scala-course  
The code we're looking at is here: https://github.com/Grogs/scala-course/commit/88b441930983a3665e71c4fe49162810510b9ade

However, there are a few bugs you can fix:
* Currently, users are only added if they change the search of the page.  
  Instead, change the frontend (App.scala) to send the location on page load, and update when they users changes destination.
* Users are not being removed when they leave/close the page. Take a look at the UserActor in UserTrackingController.scala
