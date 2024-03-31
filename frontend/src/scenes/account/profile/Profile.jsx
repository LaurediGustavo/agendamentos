import React, { useState } from 'react';
import { Box, Grid, Card, CardContent, Tab, Tabs, Divider } from "@mui/material";
import Header from "../../../components/headers/Headers";
import ProfileOverview from "../profileOverview/ProfileOverview";
import ProfileEdit from "../profileEdit/ProfileEdit";
import ChangePassword from "../changePassword/ChangePassword";
import minhaImagem from '../../../assets/nulo.jpg';
import "./profile.scss";

const Profile = () => {
    const [activeTab, setActiveTab] = useState(0);
    const [profileData, setProfileData] = useState({
        nome: 'Rodrigo',
        sobrenome: 'Prado',
        email: 'rodrigo@example.com',
        genero: 'Masculino',
        cpf: '123.456.789-00',
        telefone: '(11) 1234-56789',
        endereco: 'Rua Exemplo',
        dataNascimento: '01/01/1990',
        cep: '12345-678',
        bairro: 'Centro',
        logradouro: 'Avenida Exemplo',
        numero: '123',
        bloco: 'A'
    });

    const [profileImage, setProfileImage] = useState(minhaImagem); 

    const handleTabChange = (event, newValue) => {
        setActiveTab(newValue);
    };

    const handleSaveProfile = (editedProfile) => {
        setProfileData(editedProfile);
    };

    const handleProfileImageChange = (newImage) => {
        setProfileImage(newImage); 
    };

    return (
        <Box className="page-container">
            <Box className="profile-container">
                <Box className="profile-header">
                    <Header title="Perfil" subtitle="Visualize e edite suas informações pessoais." />
                </Box>
                <Grid container spacing={2}>
                    <Grid item xs={12} md={4}>
                        <Card className="profile-card" sx={{ margin: '0 0 30px', border: 'none', borderRadius: '5px', boxShadow: '0px 0 30px rgba(1, 41, 112, 0.1)' }}>
                            <CardContent className="profile-card-content">
                                <Box display="flex" flexDirection="column" alignItems="center">
                                    <img src={profileImage} alt="Profile" className="profile-img" /> 
                                    <span className="profile-name">{profileData.nome} {profileData.sobrenome}</span>
                                    <span className="profile-role">Administrador</span>
                                </Box>
                            </CardContent>
                        </Card>
                    </Grid>
                    <Grid item xs={12} md={8}>
                        <Card className="profile-card" sx={{ margin: '0 0 30px', border: 'none', borderRadius: '5px', boxShadow: '0px 0 30px rgba(1, 41, 112, 0.1)' }}>
                            <CardContent className="profile-card-content">
                                <Tabs value={activeTab} onChange={handleTabChange} variant="fullWidth" className="profile-tabs">
                                    <Tab label="Visão geral" style={{ color: '#11b5bb' }} />
                                    <Tab label="Editar Perfil" style={{ color: '#11b5bb' }} />
                                    <Tab label="Alterar a senha" style={{ color: '#11b5bb' }} />
                                </Tabs>
                                <Divider />
                                <Box mt={2} className="profile-tabs-content">
                                    {activeTab === 0 && <ProfileOverview profileData={profileData} />}
                                    {activeTab === 1 && <ProfileEdit profileData={profileData} onSave={handleSaveProfile} onImageChange={handleProfileImageChange} />} 
                                    {activeTab === 2 && <ChangePassword />}
                                </Box>
                            </CardContent>
                        </Card>
                    </Grid>
                </Grid>
            </Box>
        </Box>
    );
};

export default Profile;
