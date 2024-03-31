## Login widget

### Slika

> Slika (poimenujes elemente in se sklicujes potem na njih v opisu)

#### As developer I want to login

##### Successful Application Login

** PRECONDITIONS that will repeat constantly remove from test cases **

- **Given** that the user is on the Db Connection input form.
- **When** the user enters a valid database name and URL.
- **And** selects a supported DBMS type from the dropdown.
- **And** clicks the login button.
- **Then** the system validates the database URL.
- **And** the user is successfully logged into the application.
- **And** receives a confirmation message about the successful connection in the left-side logs area.

##### Login Failure Due to Incorrect Database Details

- **Given** that the user is on the Db Connection input form.
- **When** the user enters a database file instead of database URL.
- **And** selects a supported DBMS type from the dropdown.
- **And** clicks the login button.
- **Then** the system validates the database URL.
- **And** the login attempt fails.
- **And** the user receives an error message with the reason for the failure in the left-side logs area.

#### As developer I want to clear login widget
...
