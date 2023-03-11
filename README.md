<a name="readme-top"></a>

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![License][license-shield]][license-url]

<div align="center">
<h3 align="center">IBO</h3>
  <p align="center">
    IBO stands for "Internal binary operations" and it is a library for Java to read, write and handle binary files and data types that aren't officially in Java.
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

IBO stands for "Internal binary operations" and it is a library for Java to read, write and handle binary files and data types that aren't officially in Java.

Currently supported data types:

| Signed / Unsigned | Data type | Minimum Value        | Maximum Value        |
|-------------------|-----------|----------------------|----------------------|
| Signed            | Int8      | -128                 | 127                  |
| Unsigned          | UInt8     | 0                    | 255                  |
| Signed            | Int16     | -32768               | 32767                |
| Unsigned          | UInt16    | 0                    | 65535                |
| Signed            | Int32     | -2147483648          | 2147483647           |
| Unsigned          | UInt32    | 0                    | 4294967295           |
| Signed            | Int64     | -9223372036854775808 | 9223372036854775807  |
| Unsigned          | UInt64    | 0                    | 18446744073709551615 |

More data types might be added in the future.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

To install the library follow these steps:

### Prerequisites

* The library was built with Java 8

### Installation

_Manual_

Go to the <a href="https://github.com/psychose-club/IBO/releases/">Releases</a> and download the latest version of the library.

_Maven_
```
<dependency>
    <groupId>club.psychose</groupId>
    <artifactId>ibo</artifactId>
    <version>1.0.3</version>
</dependency>
```

_Gradle_

Long:
```
implementation group: 'club.psychose', name: 'ibo', version: '1.0.3'
```

Short:
```
implementation 'club.psychose:ibo:1.0.3'
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- DOCUMENTATION -->
## Documentation

Here are some features that are also included with the data types.

The FileBinaryReader, MemoryBinaryReader and BinaryWriter have more features that are here not listed which are documented in the class, so look at  it if you want! :)

Also, you can look at the <a href="https://github.com/psychose-club/IBO/tree/production/src/test/java/club/psychose/testsuite/ibo/testcases/">TestCases</a> to see more in-depth test code implementations.

<h3>FileBinaryReader</h3>

_Read an Int32 from the file offset: 0x4_

```java
class Foo {
    public void bar () {
        try {
            FileBinaryReader fileBinaryReader = new FileBinaryReader(4096); // <- The bytes that should be loaded as chunks into the memory.

            fileBinaryReader.open(Paths.get("FILE_PATH"));
            
            if (!(fileBinaryReader.isClosed())) {
                fileBinaryReader.setByteOrder(ByteOrder.BIG_ENDIAN); // <- Understanding the bytes as BIG_ENDIAN.

                fileBinaryReader.setOffsetPosition(0x4); // Skips the first bytes.

                Int32 int32 = fileBinaryReader.readInt32();

                if (int32.getValue() == 1337) { // Bytes that got read as HEX: 00 00 05 39
                    System.out.println("It's working!");
                }

                fileBinaryReader.close();
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            System.out.println("Oh no! A value is out of bounds!");
            rangeOutOfBoundsException.printStackTrace();
        } catch (ClosedException closedException) {
            System.out.println("Oh no! The reader is closed but shouldn't be!");
            closedException.printStackTrace();
        } catch (OpenedException openedException) {
            System.out.println("Oh no! The reader is opened but shouldn't be!");
            openedException.printStackTrace();
        } catch (IOException ioException) {
            System.out.println("Oh no! An IOException occurred while reading the file into chunks!");
            ioException.printStackTrace();
        }
    }
}
```
<h3>MemoryBinaryReader</h3>

_Read an UInt16 from the memory offset: 0x0_

```java
class Foo {
    public void bar () {
        byte[] bytesThatShouldBeRead = new byte[2];
        bytesThatShouldBeRead[0] = (byte) 0x94;
        bytesThatShouldBeRead[1] = 0x01;

        try {
            MemoryBinaryReader memoryBinaryReader = new MemoryBinaryReader(ByteOrder.LITTLE_ENDIAN); // <- Understanding the bytes as LITTLE_ENDIAN.

            memoryBinaryReader.open(bytesThatShouldBeRead);

            if (!(memoryBinaryReader.isClosed())) {
                UInt16 uInt16 = memoryBinaryReader.readUInt16();

                if (uInt16.getValue() == 404) {
                    System.out.println("It's working!");
                }

                memoryBinaryReader.close();
            }
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            System.out.println("Oh no! A value is out of bounds!");
            rangeOutOfBoundsException.printStackTrace();
        } catch (ClosedException closedException) {
            System.out.println("Oh no! The reader is closed but shouldn't be!");
            closedException.printStackTrace();
        } catch (OpenedException openedException) {
            System.out.println("Oh no! The reader is opened but shouldn't be!");
            openedException.printStackTrace();
        }
    }
}
```

<h3>BinaryWriter</h3>

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
            BinaryWriter binaryWriter = new BinaryWriter();
            binaryWriter.setByteOrder(ByteOrder.BIG_ENDIAN); // Writes the bytes as BIG_ENDIAN.

            binaryWriter.open(Paths.get("FILE_PATH"), false); // overwrite = false; Writes to the end of the file when the file already exists!
            binaryWriter.enableChunkPadding(0x0F, (byte) 0x0);
            binaryWriter.write(uInt8); // Written bytes: 05 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
            binaryWriter.close();
        } catch (OpenedException openedException) {
            System.out.println("Oh no! The writer is opened but shouldn't be!");
            openedException.printStackTrace();
        } catch (ClosedException closedException) {
            System.out.println("Oh no! The writer is closed but shouldn't be!");
            closedException.printStackTrace();
        } catch (IOException ioException) {
            System.out.println("Oh no! Something goes wrong while writing the file!");
            ioException.printStackTrace();
        } catch (RangeOutOfBoundsException rangeOutOfBoundsException) {
            System.out.println("Oh no! A value is out of bounds!");
            rangeOutOfBoundsException.printStackTrace();
        }
    }
}
```

_Writes a String to a file without padding_

```java
class Foo {
    public void bar () {
        String stringToWrite = "Support human rights!";

        try {
            BinaryWriter binaryWriter = new BinaryWriter();
            
            // If no ByteOrder is set the nativeOrder will be used.
            // For this example we will handle it as the BIG_ENDIAN byte order.

            binaryWriter.open(Paths.get("FILE_PATH"), true); // overwrite = true; Replaces the file with new bytes if it's already exist!
            binaryWriter.write(stringToWrite); // Written bytes: 53 75 70 70 6F 72 74 20 68 75 6D 61 6E 20 72 69 67 68 74 73 21
            binaryWriter.close();
        } catch (OpenedException openedException) {
            System.out.println("Oh no! The writer is opened but shouldn't be!");
            openedException.printStackTrace();
        } catch (ClosedException closedException) {
            System.out.println("Oh no! The writer is closed but shouldn't be!");
            closedException.printStackTrace();
        } catch (IOException ioException) {
            System.out.println("Oh no! Something goes wrong while writing the file!");
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

Stay safe and make the  world to a better place!

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
