# file-type-converter

It is just a basic service that convert file type to different type.
For instance convert pdf file to an image of type jpg or png.

Pattern I used:
- Singleton: to decrease the amount of objects created in memory.
- Dependency Injection: to decrease the coupling, also to control of the flow based on the input at runtime (to make it more dynamic).
- The Chain of responsibility pattern: which Make the request decide what class should use for converting the file.


>maybe the last pattern was not applied in this project, but For what I know I think I applied it in the decision of deciding what class to make the conversion.
