package com.droplite.constant;

public class FileConstants {

    private FileConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String DEFAULT_SORT_FIELD = "createdAt";
    public static final String DEFAULT_SORT_ORDER = "DESC";
    public static final String SUCCESS_MSG_FILE_UPLOADED_SUCCESSFULLY = "File uploaded successfully";
    public static final String SUCCESS_MSG_FILES_FETCHED_SUCCESSFULLY = "Files fetched successfully";
    public static final String SUCCESS_MSG_FILES_DELETED_SUCCESSFULLY = "Files deleted successfully";
    public static final String ERROR_MSG_FILE_UPLOAD_FAILED = "Failed to upload file";
    public static final String ERROR_MSG_FILE_DELETE_FAILED = "Failed to delete file";
    public static final String ERROR_MSG_FILE_NOT_FOUND = "File not found";
    public static final String ERROR_MSG_FILE_EXCEEDS_SIZE_LIMIT = "File should be under %s MB";
    public static final String ERROR_MSG_FILE_NOT_SUPPORTED = "File format not supported";
    public static final String ERROR_MSG_PAGE_SIZE_TOO_MUCH = "Can't display more than 50 results";
}
