# HTML 2 PDF :: How HTML tags are mapped onto iText layout objects


## intro

talk about iText layout and compare it to html

explicitly mention that we made certain decisions similar to how html is structure (nested paragraphs e.g.)

mention different mappings since not every HTML tag has a PDF/iText equivalent

include table that shows interesting mappings



## technical explanation

show where the mapping happens in the entire process

4 steps to parse all nodes

include Sam's diagram

use example to demonstrate the 4 steps


## customizing the mapping

intro on why to customize the mapping

show example on how to map a random tagworker onto a random tags

talk about better use cases: lead up to custom tagworkers



### custom tagworkers

intro sample

show code to implement a custom tagworker

explain different methods to implement


## outro

summarize