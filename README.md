# Programming for Security Project Spring 2020

## Introduction
This is app will feature an graphical user interface, which will allow users to create their own profiles and encrypt/decrypt the files they own. The profiles will have passwords, which will be securely stored with hashing and salts. Users will be required to use strong passwords. The files will be encrypted using the advanced encryption standard, which features symmetric encryption with cipher block chaining and initialization vectors. The symmetric keys will also be stored securely using hashing. If there is time, users may be to securely share their symmetric keys, so they can share their encrypted files.

##Security Goals
The primary purpose of this application is to allow the users to ensure the confidentiality of their files. In other terms, the information in the files should never be made available to any unauthorized entities.

Possible threats to the confidentiality of the user data could be:
1. Unauthorized actors who get access to the machine either physically or remotely. This could be a hacker, a spouse, a colleague, a thief or similar.
2. Technically authorized actors whom the user wishes to to hide the data from. This could be a cloud storage service provider, applications with access to the data, a technical supporter or similar.

##Security Design Patterns

##Tests
####Case 1: Authorization
The user opens up the application and is prompted to supply a secure password of at least 10 characters, that must contain upper and lower case letters, numbers and special characters. If the user supplies a password which fulfills the criteria, they are then allowed access to the program. Otherwise they are rejected and prompted to supply a secure password again.

__Demonstration__

####Case 2: Encryption
The user supplies the program with a directory path and presses the encrypt button. The program then encrypts all the data of the files, writes it to new files and deletes the originals. Now there must be no trace of the original data and the encrypted files must be unreadable.

__Demonstration__

Lorem ipsum.
####Case 3: Decryption
The user supplies the program with a directory path and presses the decrypt button instead. The program then decrypts the data of the files, writes it to now files and deletes the encrypted files. The files must now be readable and appear exactly as the original files.

__Demonstration__

Lorem ipsum.
##Appendices

#### Assignment Requirements
The assignment text 
1) The description should include a discussion of what kind of security goals are involved in the purpose (eg,confidentiality) and what threats may face the application. 
2) The description should also discuss one or more security design patterns used in the program. If the program does not use a design pattern, there should be a discussion of which design patterns you have considered (and eventually rejected).
3) The description should discuss a test of the application. Preferably this should be in the form of defining and executing a small number (say two-three) of test cases.
4) The description must use graphical or visual means of presentation. The description must include screen shots of the application (say of input and output related to testing). The description must also include at least one UML diagram, for instance a class diagram.

The source code listing (the program)
1. Clarity and sophistication of the program text.
2. You are welcome to use stubs for parts of the functionality, to reduce the programming effort.