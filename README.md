# Message encrypter
This is a message encrypting app which allows you to encrypt a message of any kind within a .png image without making any visible change to the image itself. 
It is able to do so by modifying the least significant bits of image pixels' blue values.

## How to use?

### 1. Setup
After running the app, some setup needs to be performed. Three folders need to be specifed: 
- root directory (absolute path to an existing or non-existing directory) e.g D:\my-files\message-encrypter-images
- input images directory within the root directory (name of directory containing images in which the image will be encrypted) e.g input
- output images directory within the root directory (name of directory containing images with encrypted messages) e.g. output
Type setup in order to specify all three mentioned pieces of information or setup --rec to specify only the root directory. In case of setup --rec, the default directories will be named input and output accordingly.

<div align="center">
  <img src="https://github.com/IrinUvis/message-encrypter/blob/main/readme_images/setup.png" />
  <p>Fig. 1. Example of successful setup.</p>
</div>

### 2. Encrypt a message within image
After the setup, your 'input' and 'output' directories will be empty. You need to manually add an image with .png extension. The extension is very important as you can only use these images to encrypt the messages and other files won't be recognized. The list of .png files can be displayed by typing either config info --in or config info --out.

<div align="center">
  <img src="https://github.com/IrinUvis/message-encrypter/blob/main/readme_images/no_input_images.png" />
  <p>Fig. 2. No images with .png files exist in the input directory.</p>
</div>

<div align="center">
  <img src="https://github.com/IrinUvis/message-encrypter/blob/main/readme_images/after_adding_input_image.png" />
  <p>Fig. 3. After adding a .png file, it is listed by the 'config info --in'.</p>
</div>

Now the message can be encrypted in the image. It can be done with 'hide' command and providing required information i.e.:
- name of the image from input directory. Typing '.png' is not neccessary.
- name of the image that will contain encrypted message. It will be saved in output directory. Again typing '.png' can be omitted. In case the suffix was ommited it will be added automatically.
- message to be encoded in the image
- password neccessary to decode the message from the image later on

<div align="center">
  <img src="https://github.com/IrinUvis/message-encrypter/blob/main/readme_images/hiding_operation.png" />
  <p>Fig. 4. Example of successful hiding operation.</p>
</div>

### 3. Decrypt the message from within the image
Now there should exist one file in the input directory called 'Spaceflute.png' and one file in the output directory called 'example.png'. Just by looking at them it's impossible to spot the difference and suspect that a hidden message could be hidden inside.

Input image             |  Output image with hidden message
:-------------------------:|:-------------------------:
![](https://github.com/IrinUvis/message-encrypter/blob/main/readme_images/example_input.png)  |  ![](https://github.com/IrinUvis/message-encrypter/blob/main/readme_images/example_output.png)

In order to decrypt message from withing the output image, 'show' command has to be used and required information has to be provided i.e.:
- name of the image from the output directory. Typing '.png' is not neccessary
- password that was used to encrypt the message in the original message. If wrong password is provided, a malformed message will be displayed

<div align="center">
  <img src="https://github.com/IrinUvis/message-encrypter/blob/main/readme_images/showing_operation.png" />
  <p>Fig. 5. Show command and examples of restored messages when both correct and incorrect passwords were provided.</p>
</div>

## Summary
And that is all that there is to the app. It's a simple way to encode data that shouldn't be visibile to anyone but you in a image that no one will suspect to have any special contents and even if they knew that they would still need to know the encrypting password.
