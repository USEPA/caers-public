/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.util;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Consumer;

public class TempFile implements AutoCloseable {

    private final File file;

    private final String fileName;
    
    private TempFileBlob tempFileBlob;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private TempFile(@NotNull File file, @NotNull String fileName) {

        super();

        Preconditions.checkNotNull(file, "File can not be null.");

        this.file = file;
        this.fileName = fileName;
    }

    public static TempFile create(@NotNull String fileName) {

        return create(fileName, ".tmp");
    }

    public static TempFile create(@NotNull String fileName, @NotNull String suffix) {

        File file = null;

        try {

            file = File.createTempFile(fileName, suffix);

        } catch (IOException e) {

            throw new IllegalStateException(e);
        }

        return new TempFile(file, fileName);
    }

    public static TempFile from(@NotNull InputStream inputStream, String fileName) {

        File file = null;

        try {

            file = File.createTempFile(fileName, ".tmp");
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {

            if (file != null) {
                new FileCloser().accept(file);
            }

            throw new IllegalStateException(e);
        }

        return new TempFile(file, fileName);
    }

    public static TempFile from(@NotNull File file, String fileName) {

        return new TempFile(file, MoreObjects.firstNonNull(Strings.emptyToNull(fileName), file.getName()));
    }

    @Override
    public void close() {

    	if (this.tempFileBlob != null) {
            try {

                this.tempFileBlob.close();
            } catch (Exception e) {

                logger.error("Unable to close blob.", e);
            }
        }
    	
    	new FileCloser().accept(file);
    }
    
    public TempFileBlob createBlob() throws FileNotFoundException {

        if (this.tempFileBlob == null) {

            this.tempFileBlob = new TempFileBlob(this);
        }

        return this.tempFileBlob;
    }


    public File getFile() {

        return file;
    }

    public String getFileName() {

        return fileName;
    }
    
    public long length() {

        return this.file.length();
    }

    public Path toPath() {

        return this.file.toPath();
    }

    private static class FileCloser implements Consumer<File> {

        private final Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public void accept(File file) {

            if (file.exists()) {

                if (file.delete() == false) {

                    logger.warn("Unable to delete file {}, will attempt to deleteOnExit.", file.getAbsolutePath());
                    file.deleteOnExit();
                }
            }
        }
    }
}
