// Header.js
import React from 'react';
import { Typography } from '@mui/material';

const Header = () => (
  <div style={{ textAlign: 'center', marginTop: '30px' }}>
    <Typography variant="h3" component="h1" style={{ fontWeight: 'bold', color: '#3f51b5', marginBottom: '20px' }}>
      DropLite
    </Typography>
  </div>
);

export default Header;