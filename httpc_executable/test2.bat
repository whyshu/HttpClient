@echo off
start httpc get -v -h Accept:text/plain "http://127.0.0.1/test3.txt"
start httpc post -v -h Accept:text/plain --d "{Write Request:2}" "http://127.0.0.1/test3.txt"
