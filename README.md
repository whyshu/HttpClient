# HttpClient
java -cp .;json-20180813.jar HttpClientMain post -v -h Content-Type:application/json -h Accept:text/plain -h Content-Type:text/plain -h Accept:text/plain -h Accept:application/json -h Content-Type:application/json --d "{"Assignment":1}" http://httpbin.org/post


java -cp .;json-20180813.jar HttpClientMain get -v -h Content-Type:application/json -h Accept:text/plain -h Content-Type:text/plain -h Accept:text/plain http://httpbin.org/get

java -cp .;json-20180813.jar HttpClientMain post -v -h Content-Type:application/json -h Accept:text/plain -h Content-Type:text/plain -h Accept:text/plain --d "{"Assignment":1}" http://httpbin.org/post