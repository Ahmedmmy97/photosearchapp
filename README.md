# Photo Search App
Like a search engine which can search for photos on google images or flickr and display 100 result(can be increased).

## Google Images 
-creates a search url from query value then send an http request which return result page. 
-using jsoup it query the recieved document for the div where search result urls are Stored.
-retrieving urls and showing them using Glide.

## Flicker
-uses flickr api to search results in form of  json object. 
-convert json object to java object conataining photos details.
-create static url for each photo and uses Glide to show the photo.
 
