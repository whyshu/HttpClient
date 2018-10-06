F:\eclipse_workspace\HttpClient\src>curl -X GET -v "http://blogspot.com"
Note: Unnecessary use of -X or --request, GET is already inferred.
* Rebuilt URL to: http://blogspot.com/
*   Trying 172.217.13.169...
* TCP_NODELAY set
* Connected to blogspot.com (172.217.13.169) port 80 (#0)
> GET / HTTP/1.1
> Host: blogspot.com
> User-Agent: curl/7.55.1
> Accept: */*
>
< HTTP/1.1 301 Moved Permanently
< Location: http://www.blogger.com/
< Content-Type: text/html; charset=UTF-8
< X-Content-Type-Options: nosniff
< Date: Tue, 02 Oct 2018 06:21:52 GMT
< Expires: Thu, 01 Nov 2018 06:21:52 GMT
< Server: sffe
< Content-Length: 220
< X-XSS-Protection: 1; mode=block
< Cache-Control: public, max-age=2592000
< Age: 325665
<
<HTML><HEAD><meta http-equiv="content-type" content="text/html;charset=utf-8">
<TITLE>301 Moved</TITLE></HEAD><BODY>
<H1>301 Moved</H1>
The document has moved
<A HREF="http://www.blogger.com/">here</A>.
</BODY></HTML>
* Connection #0 to host blogspot.com left intact

F:\eclipse_workspace\HttpClient\src>java -cp .;json-20180813.jar HttpClientMain get -v "http://blogspot.com"
HTTP/1.0 302 Moved Temporarily
P3P: CP="This is not a P3P policy! See https://www.google.com/support/accounts/bin/answer.py?hl=en&answer=151657 for more info."
Location: https://www.blogger.com/
Content-Type: text/html; charset=UTF-8
Date: Sat, 06 Oct 2018 00:49:41 GMT
Expires: Sat, 06 Oct 2018 00:49:41 GMT
Cache-Control: private, max-age=0
X-Content-Type-Options: nosniff
X-Frame-Options: SAMEORIGIN
X-XSS-Protection: 1; mode=block
Server: GSE
Accept-Ranges: none
Vary: Accept-Encoding


<HTML>
<HEAD>
<TITLE>Moved Temporarily</TITLE>
</HEAD>
<BODY BGCOLOR="#FFFFFF" TEXT="#000000">
<H1>Moved Temporarily</H1>
The document has moved <A HREF="https://www.blogger.com/">here</A>.
</BODY>
</HTML>
