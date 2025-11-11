# ImageEditor

ImageEditor is a simple Java program for basic image manipulation.  
It allows you to load an image either from a **local file path** or from a **URL**, then automatically generates a series of processed output images.

## Features

The program performs the following operations on the input image:

| Output File          | Description |
|---------------------|-------------|
| `trimmed.jpg`       | Removes a 60-pixel border from all sides |
| `negative.jpg`      | Applies negative color effect |
| `stretched.jpg`     | Horizontally stretches the image by duplicating pixel columns |
| `shrank.jpg`        | Vertically shrinks the image by skipping every second row |
| `inverted.jpg`      | Flips the image both vertically and horizontally |
| `colored.jpg`       | Applies a color filter (adjusts RGB channels) |

Additionally, the program generates synthetic images:

| Output File             | Description |
|-------------------------|-------------|
| `random_img.jpg`        | A randomly colored 500×500 image |
| `rectangle.jpg`         | The random image with a single yellow rectangle drawn on it |
| `generated_rect.jpg`    | The random image with 1000 randomly generated rectangles |

## Input

Before running the program, you must create a file named **`input.txt`** in the same directory as the `.java` file.

Inside `input.txt`, write one line containing either:
- The **path to a local image**, e.g.:
./image.jpg

- Or an **image URL**, e.g.:
https://example.com/example.jpg


## Requirements

- Java 8 or newer
- No external libraries needed (only uses standard Java API)

## How to Run

1. Compile:
 javac ImageEditor.java

Run:
java ImageEditor

The processed output images will appear in the same directory.

Notes
The program supports .jpg, .jpeg, .png, and other formats readable by standard ImageIO.

If an image fails to load, check:
The file path is correct
The URL is direct to an image
input.txt exists in the program folder

Project Structure Example
project-folder/
│ ImageEditor.java
│ input.txt
│ image.jpg (optional example input)
└ (output images will be created here)

Done. Images saved in project directory.
