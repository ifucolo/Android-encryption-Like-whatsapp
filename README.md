# Android Encryption Like Whats App
This is a project to help Android/Kotlin developers to create safe encryption messages between users:

 * [DH - Diffie Helman](https://hackernoon.com/algorithms-explained-diffie-hellman-1034210d5100)
 * [AES 256](https://searchsecurity.techtarget.com/definition/Advanced-Encryption-Standard)
 * [CBC](https://searchsecurity.techtarget.com/definition/cipher-block-chaining)
 * [PKCS7Padding](https://en.wikipedia.org/wiki/Padding_(cryptography))
 * [SHA-1](https://en.wikipedia.org/wiki/SHA-1)
 * [MD5](https://searchsecurity.techtarget.com/definition/MD5)
 
 Demo: 

 ![](https://github.com/ifucolo/android-encryption-like-whatsapp/blob/master/rsz_screenshot_1568237127.png)

 
## Getting Started

First, you need to add in your project the [GenerateMyKey](link) or copy the methods and the data class Keys to generate the keys from your device because we need a publicKey and a privateKey to make the encryption.

Second copy the class [AES](LINK) for your project, and to start to **encrypt** or **decrypt** any message you need to initialize it using the **public key** for an external user and your **private key**, also you can use a string for the **public key** because sometimes could come from server like a string and for that you just need to see the comment above the constructor on [AES](LINK).

See [MainActivity](LINK) get the complete example of this implementation.


### Kotlin Multiplatform
It's independent of platform and you can use for hybrid projects with kotlin multiplatform libraries.


### Why it is not a Library?
Because the idea is not limited to you with changes because you also can change algorithms or other things to fit with your project.




## LICENSE

```
The MIT License (MIT)

Copyright (c) 2019 Iago Fucolo

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
