import React from 'react';

const CentralizarHorizontalmente = ({ children }) => {
  return (
    <div style={{ display: 'flex', justifyContent: 'center' }}>
      {children}
    </div>
  );
};

export default CentralizarHorizontalmente;
