/*
 * © Copyright 2019 EPA CAERS Project Team
 *
 * This file is part of the Common Air Emissions Reporting System (CAERS).
 *
 * CAERS is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 *
 * CAERS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with CAERS.  If 
 * not, see <https://www.gnu.org/licenses/>.
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
