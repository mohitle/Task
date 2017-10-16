"# Task" 

This project contains a single class for a simple web crawler program.
It allows to extract URL contained in HTML file, reading from a URL the user can enter from console. Only links in <a></a> tags have been considered.

The class:

  - 1 method getAllLinks() to read from console the URL input by the user: the HTML source is parsed and all URLs are listed. This method is helped by extractTagAText() and extractLink().
  
  - 2 methods, extractTagAText() and extractLink(), to (i) extract the text inside <a href></a> from HTML line and (ii) extract valid URL from that. 
  
  - 1 method to choose unique domains from the list of all links (returned by getAllLinks()) and to record the number of occurrences of each domain. This method is helped by getDomain().
  
  - 1 method to extract domain name from URL, getDomain().
  
  To improve.
  - By now the program considers as link also links commented.
  - By now the program can detect a link in <a></a> also if the tag is in the previous line, but if the text of the URL is split across more than one line, either it can't detect the URL or it cut it up to the end of current line.
  - Only link starting with http(s) have been considered for this occasion.
