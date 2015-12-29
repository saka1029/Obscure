Class
=====

Senario
-------

```lisp
(define (Person name)
    (define tel false)
    (define (greeting x) (list x name))
    (new)  )
```
->
```
Closuer Person
    Obj parms = (name)
    Obj body  = ((define tel false) (define ...) (new))
    Env env   = {person outer}
```
->
```
(define jhon (Person 'JhonDoe))
```
->
```
Instance jhon

```



aaa
