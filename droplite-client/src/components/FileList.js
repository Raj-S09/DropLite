import React from 'react';
import axios from 'axios';
import config from '../config';
import { List, ListItem, ListItemText, ListItemIcon, Button, Typography, Divider } from '@mui/material';
import { GetApp as DownloadIcon } from '@mui/icons-material';

const FileList = ({ fileList }) => {

  // Handle file download when the download button is clicked
  const handleDownload = async (fileId, fileName) => {
    try {
      const response = await axios.get(`${config.apiBaseUrl}/${fileId}`, { responseType: 'blob' });
      const url = URL.createObjectURL(response.data);
      const link = document.createElement('a');
      link.href = url;
      link.download = fileName;
      link.click();
    } catch (error) {
      console.error('Error downloading file:', error);
    }
  };

  return (
    <div style={{ padding: '20px', maxWidth: '800px', margin: 'auto' }}>
      {fileList.length === 0 ? (
        <Typography variant="h6" align="center" style={{ width: '100%' }}>
          No files uploaded yet.
        </Typography>
      ) : (
        <List>
          {fileList.map((file) => (
            <div key={file.id}>
              <ListItem
                style={{
                  borderBottom: '1px solid #ddd',
                  padding: '15px 10px',
                  display: 'flex',
                  alignItems: 'center',
                }}
              >
                <ListItemText
                  primary={
                    <Typography
                      variant="body1"
                      style={{
                        wordWrap: 'break-word',
                        maxWidth: '300px',
                        overflow: 'hidden',
                        textOverflow: 'ellipsis',
                      }}
                    >
                      {file.fileName}
                    </Typography>
                  }
                  secondary={`Uploaded at: ${new Date(file.uploadedAt).toLocaleString()} | File Type: ${file.fileType}`}
                  style={{ flex: 1 }}
                />
                <ListItemIcon>
                  <Button
                    variant="outlined"
                    color="primary"
                    onClick={() => handleDownload(file.id, file.fileName)}
                    startIcon={<DownloadIcon />}
                  >
                    Download
                  </Button>
                </ListItemIcon>
              </ListItem>
              <Divider />
            </div>
          ))}
        </List>
      )}
    </div>
  );
};

export default FileList;