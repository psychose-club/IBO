<a name="readme-top"></a>

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![License][license-shield]][license-url]

<div align="center">
<h3 align="center">IBO</h3>
  <p align="center">
    IBO stands for "Internal binary operations" and it is a library for Java to read,
    write, and handle binary files and data types that aren't available in Java.
    <br />
    <a href="#documentation"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/psychose-club/IBO/issues">Report Bug</a>
    ·
    <a href="https://github.com/psychose-club/IBO/blob/production/SECURITY.md">Report Vulnerabilities</a>
    ·
    <a href="https://github.com/psychose-club/IBO/issues">Request Feature</a>
  </p>
</div>
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
    </li>
    <li>
        <a href="#planned-features">Planned features</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#documentation">Documentation</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#credits">Credits</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

IBO stands for "Internal binary operations" and it is a library for Java to read,
 write, and handle binary files and data types that aren't available in Java.

Currently supported data types:

| Signed / Unsigned | Data type | Minimum Value        | Maximum Value        |
|-------------------|-----------|----------------------|----------------------|
| Signed            | Int8      | -128                 | 127                  |
| Signed            | Int16     | -32768               | 32767                |
| Signed            | Int24     | -8388608             | 8388607              |
| Signed            | Int32     | -2147483648          | 2147483647           |
| Signed            | Int64     | -9223372036854775808 | 9223372036854775807  |
| Unsigned          | UInt8     | 0                    | 255                  |
| Unsigned          | UInt16    | 0                    | 65535                |
| Unsigned          | UInt24    | 0                    | 16777215             |
| Unsigned          | UInt32    | 0                    | 4294967295           |
| Unsigned          | UInt64    | 0                    | 18446744073709551615 |

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- PLANNED FEATURES -->
### Planned features
We are considering the following features to be added when enough people ask for it:
* More data types (If possible)

If you want to contribute and add some features, create an issue in the Issues tab and create a pull request! :)

For more information see <a href="#contributing">Contributing</a>

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

To install the library, follow these steps:

### Prerequisites

* JDK 8+

### Installation

_Manual_

Go to the <a href="https://github.com/psychose-club/IBO/releases/">Releases</a> and download the latest version of the library.

_Maven_
```
<dependency>
    <groupId>club.psychose</groupId>
    <artifactId>ibo</artifactId>
    <version>2.0.1</version>
</dependency>
```

_Gradle_

Long:
```
implementation group: 'club.psychose', name: 'ibo', version: '2.0.1'
```

Short:
```
implementation 'club.psychose:ibo:2.0.1'
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- DOCUMENTATION -->
## Documentation

Here are some features that are also included with the data types.

The BinaryFile has more features that are not listed here, 
but which are documented in the class, so look at it if you want! 
:)

Also, you can look at the <a href="https://github.com/psychose-club/IBO/tree/production/src/test/java/club/psychose/testsuite/ibo/testcases/">TestCases</a> to see more in-depth test code implementations.

<h3>BinaryFile</h3>

_Read an Int32 from the file offset: 0x4_

```java
class Foo {
    public void bar () {
        try {
            BinaryFile binaryFile = new BinaryFile(ByteOrder.BIG_ENDIAN);
            
            // You can also change the ByteOrder if you want.
            //binaryFile.setByteOrder(ByteOrder.BIG_ENDIAN); 

            binaryFile.open(Paths.get("FILE_PATH"), 0x0, FileMode.READ_AND_WRITE);

            if (!(binaryFile.isClosed())) {
                binaryFile.setOffsetPosition(0x4); // Sets the offset position.
                // Alternative: binaryFile.skipOffsetPosition(0x04); Skips the first four bytes.

                Int32 int32 = binaryFile.readInt32();

                if (int32.getValue() == 1337) { // Bytes that got read as HEX: 00 00 05 39
                    System.out.println("It's working!");
                }

                binaryFile.close();
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            System.out.println("Oh no! A value is out of bounds!");
            rangeOutOfBoundsException.printStackTrace();
        } catch (InvalidFileModeException invalidFileModeException) {
            System.out.println("Oh no! The file is opened in the wrong mode!");
            closedException.printStackTrace();
        } catch (ClosedException closedException) {
            System.out.println("Oh no! The file is closed but shouldn't be!");
            closedException.printStackTrace();
        } catch (OpenedException openedException) {
            System.out.println("Oh no! The file is opened but shouldn't be!");
            openedException.printStackTrace();
        } catch (IOException ioException) {
            System.out.println("Oh no! An IOException occurred!");
            ioException.printStackTrace();
        }
    }
}

```
_Writes an UInt8 to a file with padding_

```java
class Foo {
    public void bar () {
        UInt8 uInt8;

        try {
            uInt8 = new UInt8(5);
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            System.out.println("Oh no! The UInt8 was initialized with a value that is not in the data type range!");
            rangeOutOfBoundsException.printStackTrace();
            return;
        }

        try {
            BinaryFile binaryFile = new BinaryFile();
            binaryFile.setByteOrder(ByteOrder.BIG_ENDIAN); // Writes the bytes as BIG_ENDIAN.

            binaryFile.open(Paths.get("FILE_PATH"), 0x0, FileMode.READ_AND_WRITE);
            binaryFile.enablePadding(0x0F, (byte) 0x0);
            binaryFile.write(uInt8); // Written bytes: 05 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
            binaryFile.close();
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            System.out.println("Oh no! A value is out of bounds!");
            rangeOutOfBoundsException.printStackTrace();
        } catch (InvalidFileModeException invalidFileModeException) {
            System.out.println("Oh no! The file is opened in the wrong mode!");
            closedException.printStackTrace();
        } catch (ClosedException closedException) {
            System.out.println("Oh no! The file is closed but shouldn't be!");
            closedException.printStackTrace();
        } catch (OpenedException openedException) {
            System.out.println("Oh no! The file is opened but shouldn't be!");
            openedException.printStackTrace();
        } catch (IOException ioException) {
            System.out.println("Oh no! An IOException occurred!");
            ioException.printStackTrace();
        }
    }
}
```

_Write a String to a file without padding_

```java
class Foo {
    public void bar () {
        String stringToWrite = "Support human rights!";

        try {
            BinaryFile binaryFile = new BinaryFile();
            
            // If no ByteOrder is set, the nativeOrder will be used.
            // For this example, we will handle it as the BIG_ENDIAN byte order.

            binaryFile.open(Paths.get("FILE_PATH"), 0x0, FileMode.WRITE);
            binaryFile.write(stringToWrite); // Written bytes: 53 75 70 70 6F 72 74 20 68 75 6D 61 6E 20 72 69 67 68 74 73 21
            binaryFile.close();
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            System.out.println("Oh no! A value is out of bounds!");
            rangeOutOfBoundsException.printStackTrace();
        } catch (InvalidFileModeException invalidFileModeException) {
            System.out.println("Oh no! The file is opened in the wrong mode!");
            closedException.printStackTrace();
        } catch (ClosedException closedException) {
            System.out.println("Oh no! The file is closed but shouldn't be!");
            closedException.printStackTrace();
        } catch (OpenedException openedException) {
            System.out.println("Oh no! The file is opened but shouldn't be!");
            openedException.printStackTrace();
        } catch (IOException ioException) {
            System.out.println("Oh no! An IOException occurred!");
            ioException.printStackTrace();
        }
    }
}
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Checkout to the development branch (`git checkout development`)
3. Create your Feature Branch (`git checkout -b FEATURE_NAME`)
4. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
5. Push to the Branch (`git push origin FEATURE_NAME`)
6. Open a Pull Request to the development branch

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- LICENSE -->
## License

Distributed under the BSD 3-Clause License. See `LICENSE` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CREDITS -->
## Credits

Thanks to:

For the README
* [Best-README-Template](https://github.com/othneildrew/Best-README-Template)
* [Img Shields](https://shields.io)

and you <3

Stay safe, love and accept other people, please! <3

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/psychose-club/IBO.svg?style=for-the-badge
[contributors-url]: https://github.com/psychose-club/IBO/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/psychose-club/IBO.svg?style=for-the-badge
[forks-url]: https://github.com/psychose-club/IBO/network/members
[stars-shield]: https://img.shields.io/github/stars/psychose-club/IBO?style=for-the-badge
[stars-url]: https://github.com/psychose-club/IBO/stargazers
[issues-shield]: https://img.shields.io/github/issues/psychose-club/IBO.svg?style=for-the-badge
[issues-url]: https://github.com/psychose-club/IBO/issues
[license-shield]: https://img.shields.io/github/license/psychose-club/IBO.svg?style=for-the-badge
[license-url]: https://github.com/psychose-club/IBO/blob/master/LICENSE.txt
