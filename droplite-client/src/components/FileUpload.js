import React, { useState, useEffect } from 'react';
import { Button, CircularProgress, LinearProgress, Alert, Typography, Paper, Box } from '@mui/material';
import axios from 'axios';
import config from '../config';
import FileList from './FileList';

function FileUpload() {
  const [file, setFile] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [progress, setProgress] = useState(0);
  const [responseMessage, setResponseMessage] = useState('');
  const [error, setError] = useState('');
  const [fileList, setFileList] = useState([]);

  // Fetch file list from the server
  const fetchFileList = async () => {
    try {
      const response = await axios.get(`${config.apiBaseUrl}`);
      setFileList(response.data.data);
    } catch (err) {
      console.error('Error fetching file list', err);
    }
  };

  // Fetch file list when component mounts
  useEffect(() => {
    fetchFileList();
  }, []);

  // Handle file change in the upload box
  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
    setProgress(0);
    setResponseMessage('');
    setError('');
  };

  // Handle file upload
  const handleFileUpload = async () => {
    if (!file) {
      alert('Please select a file');
      return;
    }

    setUploading(true);
    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await axios.post(`${config.apiBaseUrl}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: (event) => {
          if (event.total) {
            const percent = Math.round((event.loaded * 100) / event.total);
            setProgress(percent);
          }
        },
      });

      setUploading(false);
      setResponseMessage(response.data.message); // Use the message from the API response

      // Add the new file to the existing file list
      const newFile = response.data.data;
      setFileList((prevList) => [...prevList, newFile]);

      // Refetch the file list after upload to update in real time
      fetchFileList();

    } catch (err) {
      setUploading(false);
      // Extract and display the error message from the API response
      if (err.response?.data?.message) {
        setError(err.response.data.message);
      } else {
        // Fallback error message for unexpected cases
        setError('File upload failed. Please try again.');
      }
    }
  };

  return (
    <Paper style={{ padding: '20px', display: 'flex', flexDirection: 'column', alignItems: 'center', width: '100%', maxWidth: '600px', margin: 'auto' }}>

      {uploading && <CircularProgress size={50} color="primary" style={{ marginBottom: '20px' }} />}

      <Box
        style={{
          border: '2px dashed #3f51b5',
          padding: '16px',
          borderRadius: '8px',
          width: '60%',
          maxWidth: '400px',
          textAlign: 'center',
          backgroundColor: '#f7f7f7',
          cursor: 'pointer',
          transition: 'background-color 0.3s ease, transform 0.2s ease',
          marginBottom: '10px',
          marginTop: '20px',
          wordWrap: 'break-word',
        }}
        onClick={() => document.getElementById('fileInput').click()}
        onMouseOver={(e) => (e.currentTarget.style.backgroundColor = '#e3f2fd')}
        onMouseOut={(e) => (e.currentTarget.style.backgroundColor = '#f7f7f7')}
        onFocus={(e) => (e.currentTarget.style.transform = 'scale(1.02)')}
        onBlur={(e) => (e.currentTarget.style.transform = 'scale(1)')}
      >
        <Typography variant="body1" style={{ color: '#3f51b5', fontWeight: '600' }}>
          {file ? file.name : 'Choose a file to upload'}
        </Typography>
        <input
          type="file"
          id="fileInput"
          onChange={handleFileChange}
          style={{
            display: 'none',
          }}
        />
      </Box>

      <Button
        variant="contained"
        color="primary"
        onClick={handleFileUpload}
        disabled={uploading || !file}
        style={{ marginTop: '20px', width: '80%', maxWidth: '400px' }}
      >
        Upload
      </Button>

      {progress > 0 && !uploading && (
        <LinearProgress variant="determinate" value={progress} style={{ width: '0%', marginBottom: '20px' }} />
      )}

      {responseMessage && !uploading && (
        <Alert severity="success" style={{ width: '60%' }}>
          {responseMessage} { }
        </Alert>
      )}

      {error && !uploading && (
        <Alert severity="error" style={{ width: '60%' }}>
          {error} { }
        </Alert>
      )}

      <Box mt={3} width="100%">
        <FileList fileList={fileList} /> { }
      </Box>
    </Paper>
  );
}

export default FileUpload;