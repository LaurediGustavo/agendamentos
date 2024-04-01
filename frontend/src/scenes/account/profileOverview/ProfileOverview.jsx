import React from 'react';
import { Typography } from '@mui/material';
import { useTheme } from '@mui/material/styles';
import useMediaQuery from '@mui/material/useMediaQuery';

const ProfileField = ({ label, value }) => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    return (
        <div style={{ display: 'flex', flexDirection: isMobile ? 'column' : 'row', marginBottom: '2em' }}>
            <div style={{ width: isMobile ? '100%' : '20%', marginRight: isMobile ? '0' : '5%', color: 'rgba(1, 41, 112, 0.6)', fontSize: '1.25em' }}>{label}:</div>
            <div style={{ fontSize: '1.25em' }}>{value}</div>
        </div>
    );
};

const ProfileOverview = ({ profileData }) => {
    return (
        <div style={{ overflowX: 'auto' }}>
            <Typography variant="h5" gutterBottom style={{ color: '#29686b', marginBottom: '20px', marginTop: '28px' }}>Detalhes de perfil</Typography>
            <ProfileField label="Nome" value={profileData.nome} />
            <ProfileField label="Sobrenome" value={profileData.sobrenome} />
            <ProfileField label="E-mail" value={profileData.email} />
            <ProfileField label="Gênero" value={profileData.genero} />
            <ProfileField label="CPF" value={profileData.cpf} />
            <ProfileField label="Telefone" value={profileData.telefone} />
            <ProfileField label="Data de Nascimento" value={profileData.dataNascimento} />
            <ProfileField label="CEP" value={profileData.cep} />
            <ProfileField label="Bairro" value={profileData.bairro} />
            <ProfileField label="Logradouro" value={profileData.logradouro} />
            <ProfileField label="Número" value={profileData.numero} />
            <ProfileField label="Bloco" value={profileData.bloco} />
        </div>
    );
};

export default ProfileOverview;
