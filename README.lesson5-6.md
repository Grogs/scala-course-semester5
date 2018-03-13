# Lesson 5

Today we will review the backend services we implemented last week.  
Then we will create a basic search results page.

### Tasks
* Checkout the `lesson5`branch
* Run `sbt test` to see the failing tests.
* Fix the failing tests in `lesson5.HotelsControllerSpec`.
    * To do this, you will have to build a page to show search results
    * You can check your progress by running `./sbt run` and then browsing to http://localhost:9000/hotels/search?destination=london&distance=1.2

### Guidance
* We have a `HotelsController` with the `HotelsService` injected. Add a new search endpoint there.
* You will need to add a route in the `routes` file.
    * The destination and distance must be passed through as query parameters. Here's an example of how that works in the routes file:  
      Given the route `GET   /comments                     controllers.Application.comments(page: Int)`  
      If we called `/comments?page=1`, then `1` would be passed into the  `page` parameter
    * For more info, see docs: https://www.playframework.com/documentation/2.6.x/ScalaRouting
* You will need to add a template in the `views` package.
    * Docs for Play's templates: https://www.playframework.com/documentation/2.6.x/ScalaTemplates
  

### Note  
**If you finish the basic search page you will need to add some CSS and JavaScript to your view to pickup bootstrap**  
To do this, add this in your template:
For lesson5 please add this to the view you created:
```
        <link rel='stylesheet' href='@routes.WebJarAssets.at(webJarAssets.locate("css/bootstrap.css"))'>
        <script src="@routes.WebJarAssets.at(webJarAssets.locate("jquery.js"))" type="text/javascript"></script>
        <script src="@routes.WebJarAssets.at(webJarAssets.locate("js/bootstrap.js"))" type="text/javascript"></script>
```

As you can see in that code fragment, we are calling `webJarAssets.locate` for the path to the CSS/JavaScript. You will need to pass the `webJarAssets`, which has been injected into the Controller, through to your view. 