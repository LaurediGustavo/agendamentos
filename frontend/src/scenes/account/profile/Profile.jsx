import React, { useState, useEffect } from 'react';
import { Box, Grid, Card, CardContent, Tab, Tabs, Divider } from "@mui/material";
import Header from "../../../components/headers/Headers";
import ProfileOverview from "../profileOverview/ProfileOverview";
import ProfileEdit from "../profileEdit/ProfileEdit";
import ChangePassword from "../changePassword/ChangePassword";
import minhaImagem from '../../../assets/nulo.jpg';
import "./profile.scss";
import api from '../../../services/api';
import { formatarData_yyyy_MM_dd, formatarData_dd_MM_yyyy } from '../../../services/dateFormat';

const Profile = () => {
    const [activeTab, setActiveTab] = useState(0);
    const [profileData, setProfileData] = useState({
        id: 0,
        nome: '',
        sobrenome: '',
        email: '',
        genero: '',
        cpf: '',
        telefone: '',
        dataDeNascimento: '',
        cep: '',
        bairro: '',
        logradouro: '',
        numero: '',
        bloco: ''
    });

    const [profileImage, setProfileImage] = useState(minhaImagem); 

    const usuario = async () => {
        try {
          const response = await api.get("/usuario/usuariologado");
          const user = response.data;
          const usuario = {
              id: user.id,
              nome: user.nome,
              sobrenome: user.sobrenome,
              email: user.email,
              genero: user.genero,
              cpf: mascaraCpf(user.cpf),
              telefone: user.telefone,
              dataDeNascimento: formatarData_dd_MM_yyyy(user.dataDeNascimento),
              cep: user.cep,
              logradouro: user.logradouro,
              bairro: user.bairro,
              numero: user.numero,
              bloco: user.bloco,
          };
          setProfileData(usuario);
        } catch (error) {
          console.error("Ops! Ocorreu um erro: " + error);
        }
    };

    const imagem = async () => {
        try {
            const response = await api.get("/usuario/imagem", { responseType: 'arraybuffer' });

            if (response.data.byteLength > 0) {
                const blob = new Blob([response.data], { type: 'image/png' }); // ou 'image/png', dependendo do tipo de imagem
                const imageUrl = URL.createObjectURL(blob);
                setProfileImage(imageUrl);
            }
        } catch (error) {
          console.error("Ops! Ocorreu um erro: " + error);
        }
    };

    const mascaraCpf = (value) => {
        const cleanedCPF = value.replace(/[^\d]/g, '');
        return cleanedCPF.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
    }

    useEffect(() => {
        usuario();
        imagem();
    }, []);

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
