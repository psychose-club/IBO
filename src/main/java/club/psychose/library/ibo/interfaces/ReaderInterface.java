/*
 * Copyright (c) 2023, psychose.club
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package club.psychose.library.ibo.interfaces;

import club.psychose.library.ibo.core.datatypes.types.signed.Int16;
import club.psychose.library.ibo.core.datatypes.types.signed.Int32;
import club.psychose.library.ibo.core.datatypes.types.signed.Int64;
import club.psychose.library.ibo.core.datatypes.types.signed.Int8;
import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt16;
import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt32;
import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt64;
import club.psychose.library.ibo.core.datatypes.types.unsigned.UInt8;
import club.psychose.library.ibo.exceptions.ClosedException;
import club.psychose.library.ibo.exceptions.RangeOutOfBoundsException;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * The ReaderInterface is required to be implemented under all BinaryReaders and serves as structure what needs to be implemented.
 */
public interface ReaderInterface {
    // Close method.
    void close();

    // Reading methods.
    byte[] readBytes (int length) throws ClosedException, IOException, RangeOutOfBoundsException;
    Int8 readInt8 () throws ClosedException, IOException, RangeOutOfBoundsException;
    UInt8 readUInt8 () throws ClosedException, IOException, RangeOutOfBoundsException;
    Int16 readInt16 () throws ClosedException, IOException, RangeOutOfBoundsException;
    UInt16 readUInt16 () throws ClosedException, IOException, RangeOutOfBoundsException;
    Int32 readInt32 () throws ClosedException, IOException, RangeOutOfBoundsException;
    UInt32 readUInt32 () throws ClosedException, IOException, RangeOutOfBoundsException;
    Int64 readInt64 () throws ClosedException, IOException, RangeOutOfBoundsException;
    UInt64 readUInt64 () throws ClosedException, IOException, RangeOutOfBoundsException;
    float readFloat () throws ClosedException, IOException, RangeOutOfBoundsException;
    double readDouble () throws ClosedException, IOException, RangeOutOfBoundsException;
    String readString (int length) throws ClosedException, IOException, RangeOutOfBoundsException;
    String readString (int length, Charset charset) throws ClosedException, IOException, RangeOutOfBoundsException;

    // Misc.
    void setByteOrder (ByteOrder value);

    // Returnables.
    boolean isClosed ();
    ByteOrder getByteOrder ();
}