use CMD::Colors;

##### Example Usage ##### 

## Prints text with 'RED' color & default background
Cprint('hello, This is RED text', 'red');                   

## Prints text with 'RED' color & 'white' background 
Cprint('hello, This is RED text', 'red', 'white');    

## Prints text with 'RED' color & 'default' background & BOLD text
Cprint('hello, This is RED text', 'red', 'default', 'bold'); 

## Prints text with 'RED' color & 'default' background & 'half_bright' text
Cprint('hello, This is RED text', 'red', undef, 'half_bright');

