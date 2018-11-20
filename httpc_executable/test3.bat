@echo off
start httpc get -v -h Accept:text/plain "http://127.0.0.1/test3.txt"
timeout /t 0 > t.txt
start httpc get -v -h Accept:text/plain "http://127.0.0.1/test3.txt"