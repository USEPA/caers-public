/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

//borrowed from PassthroughBlob
public class TempFileBlob implements Blob, AutoCloseable {

	private InputStream binaryStream;

	private long contentLength;

	private boolean freed = false;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public TempFileBlob(TempFile tempFile) throws FileNotFoundException {

		this.binaryStream = new FileInputStream(tempFile.getFile());
		this.contentLength = tempFile.length();
	}

	@Override
	public void close() {

		free();
	}

	public void free() {

		logger.debug("Freeing blob resources.");

		if (this.binaryStream != null) {

			try {
				this.binaryStream.close();
			} catch (IOException e) {
				logger.warn("Unable to close binary stream", e);
			}

			this.binaryStream = null;
		}

		this.freed = true;
	}

	public InputStream getBinaryStream() throws SQLException {

		if (this.freed) {

			throw new SQLException("Blob is invalid; free() was called.");
		}

		return this.binaryStream;
	}

	public InputStream getBinaryStream(long pos, long length)
			throws SQLException {

		throw new UnsupportedOperationException();
	}

	public byte[] getBytes(long pos, int length) throws SQLException {

		throw new UnsupportedOperationException();
	}

	public long length() throws SQLException {

		return this.contentLength;
	}

	public long position(byte[] pattern, long start) throws SQLException {

		throw new UnsupportedOperationException();
	}

	public long position(Blob pattern, long start) throws SQLException {

		throw new UnsupportedOperationException();
	}

	public OutputStream setBinaryStream(long pos) throws SQLException {

		throw new UnsupportedOperationException();
	}

	public int setBytes(long pos, byte[] bytes) throws SQLException {

		throw new UnsupportedOperationException();
	}

	public int setBytes(long pos, byte[] bytes, int offset, int len)
			throws SQLException {

		throw new UnsupportedOperationException();
	}

	public void truncate(long len) throws SQLException {

		throw new UnsupportedOperationException();
	}
}
