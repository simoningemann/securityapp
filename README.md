# FileSafe
Programming for Security Project  
Roskilde University, Denmark  
Simon Ingemann Christensen  
Spring 2020  
Repository: https://github.com/siinch/securityapp


### Introduction
This is app features a graphical user interface, which allows users to easily encrypt and decrypt their files. The user are required to use strong passwords for password based key derivation. The files are encrypted using the advanced encryption standard with cipher block chaining and initialization vectors. Upon encryption, the original files will be deleted automatically and the encrypted files will be hidden and the file names encrypted.

### Security Goals
The primary purpose of this application is to allow the users to ensure the confidentiality of their files. In other terms, the information in the files should never be made available to any unauthorized entities.

Possible threats to the confidentiality of the user data could be:
1. A spouse, colleague or similar actor who gets access to the system.
2. A thief who steals the hard drive and tries to extract the data.
3. A hacker who somehow gets access to the data remotely.

The solution is to use a strong encryption schema and only allow for decryption if the user supplies the correct password. One weakness is of course, that if the user forgets their password, the files could be lost forever. But this application sacrifices convienience for maximum security.

### Security Design Patterns
No security design patterns were implemented in this application. Here it will be explained why.

__Single Access Point__  
This pattern was rejected because it would be redundant, since the operating system aleady has authenticated the user. This application is meant only to support the user currently logged into of the system.

__Check Point__  
Again, with only one user and the limited features of this system, the check point pattern is unnecessary.
However, in a scenario where this application were an online service with multiple user and features, these two patterns would become relevant.

__Model View Controller__  
Obviously, this application could benefit from the structure that the MVC pattern provides, to make it more extendable and maintainable. But since the main purpose of this application is to demonstrate some security mechanisms, this pattern has also been rejected.
### Tests
__Case 1: Encryption__  
The program encrypts the data of selected files, writes it to new files with encrypted and hidden names and then deletes the originals. Now there must be no trace of the original data and the encrypted files must be unreadable.

__Demonstration__  
Lorem ipsum.

__Case 2: Decryption__  
The program decrypts the data of the encrypted files in the selected folder, then writes it to files with the original names and deletes the encrypted files. The files must now be readable and appear exactly as the original files.

__Demonstration__  
Lorem ipsum.

__Case 3: Reject Password__  
The program must refuse to decrypt files, for which the user has supplied the wrong password.

__Demonstration__  
Lorem ipsum.

## Appendices