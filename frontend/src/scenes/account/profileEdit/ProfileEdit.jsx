import React, { useState, useEffect } from 'react';
import { Typography, TextField, Button, RadioGroup, Radio, FormControlLabel, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';
import { useTheme } from '@mui/material/styles';
import useMediaQuery from '@mui/material/useMediaQuery';
import minhaImagem from '../../../assets/nulo.jpg';
import nuloImg from '../../../assets/nulo.jpg'; // Importando a imagem padrão
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import DeleteIcon from '@mui/icons-material/Delete';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import api from '../../../services/api';
import { formatarData_yyyy_MM_dd, formatarData_dd_MM_yyyy } from '../../../services/dateFormat';

const ProfileField = ({ label, value, error }) => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    return (
        <div style={{ display: 'flex', flexDirection: isMobile ? 'column' : 'row', alignItems: 'center', marginBottom: '2em' }}>
            <div style={{ flexBasis: isMobile ? '100%' : '30%', color: 'rgba(1, 41, 112, 0.6)', fontSize: '1.25em' }}>{label}</div>
            <div style={{ flexBasis: '70%' }}>
                {value}
                {error && <Typography variant="caption" color="error">{error}</Typography>}
            </div>
        </div>
    );
};

const ProfileEdit = ({ profileData, onSave, onImageChange }) => {
    const [openDialog, setOpenDialog] = useState(false);
    const [image, setImage] = useState(minhaImagem);
    
    const handleSave = () => {
        setOpenDialog(true);
    };
    
    const handleCloseDialog = () => {
        setOpenDialog(false);
    };
    
    const imagem = async () => {
        try {
            const response = await api.get("/usuario/imagem", { responseType: 'arraybuffer' });
    
            if (response.data.byteLength > 0) {
                const blob = new Blob([response.data], { type: 'image/png' }); // ou 'image/png', dependendo do tipo de imagem
                const imageUrl = URL.createObjectURL(blob);
                setImage(imageUrl);
            }
        } catch (error) {
          console.error("Ops! Ocorreu um erro: " + error);
        }
    };

    useEffect(() => {
        imagem();
    }, []);
    
    const handleImageUpload = async (event) => {
        const file = event.target.files[0];
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = () => {
            setImage(reader.result);
            onImageChange(reader.result);
        };

        const formData = new FormData();
        formData.append('image', file);

        try {
            await api.put('usuario/alterarimagem', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            console.log('Imagem enviada com sucesso');
        } catch (error) {
            console.error('Erro ao enviar imagem:', error.message);
        }
    };

    const handleImageRemove = async () => {
        try {
            await api.put('usuario/removerimagem');
            console.log('Imagem removida com sucesso');
        } catch (error) {
            console.error('Erro ao enviar imagem:', error.message);
        }

        setImage(nuloImg);
        onImageChange(nuloImg);
    };

    const validationSchema = Yup.object({
        nome: Yup.string().required('Campo obrigatório'),
        sobrenome: Yup.string().required('Campo obrigatório'),
        email: Yup.string().email('Formato de e-mail inválido').required('Campo obrigatório'),
        genero: Yup.string().required('Campo obrigatório'),
        cpf: Yup.string().matches(/^\d{3}\.\d{3}\.\d{3}\-\d{2}$/, 'Formato de CPF inválido').required('Campo obrigatório'),
        telefone: Yup.string().required('Campo obrigatório'),
        dataNascimento: Yup.string().required('Campo obrigatório'),
        cep: Yup.string().required('Campo obrigatório'),
        bairro: Yup.string().required('Campo obrigatório'),
        logradouro: Yup.string().required('Campo obrigatório'),
        numero: Yup.number().required('Campo obrigatório'),
        bloco: Yup.string(),
    });

    const formik = useFormik({
        initialValues: profileData,
        validationSchema: validationSchema,
        onSubmit: (values) => {
            onSave(values);
            handleCloseDialog();

            atualizar(values);
        },
    });

    const atualizar = async (values) => {
        try {
          await api.put("/usuario/alterarusuario", {
            id: values.id,
            nome: values.nome,
            sobrenome: values.sobrenome,
            dataDeNascimento: formatarData_yyyy_MM_dd(values.dataNascimento),
            genero: values.genero,
            telefone: values.telefone,
            cep: values.cep,
            logradouro: values.logradouro,
            bairro: values.bairro,
            numero: values.numero,
            bloco: values.bloco,
            email: values.email,
          });
        } catch (error) {
          throw new Error("Erro ao atualizar usuario: " + error);
        }
      };

    return (
        <div style={{ overflowX: 'auto' }}>
            <Typography variant="h5" gutterBottom style={{ color: '#29686b', marginBottom: '20px', marginTop: '28px' }}>Editar Perfil</Typography>
            <form onSubmit={formik.handleSubmit}>
                {/* Campo de imagem de perfil */}
                <ProfileField label="Imagem de perfil" value={
                    <div className="d-flex align-items-center">
                        <div style={{ marginRight: '2em' }}>
                            {image && <img src={image} alt="Profile" style={{ minWidth: '150px', maxHeight: '150px' }} />}
                        </div>
                        <div className="d-flex flex-column align-items-center">
                            <input
                                accept="image/*"
                                style={{ display: 'none' }}
                                id="image-upload"
                                type="file"
                                onChange={handleImageUpload}
                            />
                            <label htmlFor="image-upload">
                                <Button variant="contained" color="primary" component="span" size="small" title="Carregar nova imagem de perfil" style={{ marginRight: '0.5em', marginLeft: '0.5em' }}><CloudUploadIcon fontSize="small" /></Button>
                            </label>
                            <Button variant="contained" color="error" size="small" title="Remover minha imagem de perfil" onClick={handleImageRemove}><DeleteIcon fontSize="small" /></Button>
                        </div>
                    </div>
                } error={formik.touched.imagemPerfil && formik.errors.imagemPerfil} />
                <ProfileField label="Nome" value={<TextField id="name" name="nome" variant="outlined" value={formik.values.nome} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} />} error={formik.touched.nome && formik.errors.nome} />
                <ProfileField label="Sobrenome" value={<TextField id="surname" name="sobrenome" variant="outlined" value={formik.values.sobrenome} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} />} error={formik.touched.sobrenome && formik.errors.sobrenome} />
                <ProfileField label="E-mail" value={<TextField id="email" name="email" variant="outlined" value={formik.values.email} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} />} error={formik.touched.email && formik.errors.email} />
                <ProfileField label="Gênero" value={
                    <RadioGroup name="genero" value={formik.values.genero} onChange={formik.handleChange} style={{ display: 'flex', flexDirection: 'row' }}>
                        <FormControlLabel value="Masculino" control={<Radio style={{ color: '#3fbabf' }} />} label="Masculino" />
                        <FormControlLabel value="Feminino" control={<Radio style={{ color: '#3fbabf' }} />} label="Feminino" />
                        <FormControlLabel value="Outro" control={<Radio style={{ color: '#3fbabf' }} />} label="Outro" />
                    </RadioGroup>
                } error={formik.touched.genero && formik.errors.genero} />
                <ProfileField label="CPF" value={<TextField id="cpf" name="cpf" variant="outlined" value={formik.values.cpf} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} disabled />} error={formik.touched.cpf && formik.errors.cpf} />
                <ProfileField label="Telefone" value={<TextField id="phone" name="telefone" variant="outlined" value={formik.values.telefone} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} />} error={formik.touched.telefone && formik.errors.telefone} />
                <ProfileField label="Data de Nascimento" value={<TextField id="birthdate" name="dataNascimento" variant="outlined" value={formik.values.dataNascimento} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} />} error={formik.touched.dataNascimento && formik.errors.dataNascimento} />
                <ProfileField label="CEP" value={<TextField id="cep" name="cep" variant="outlined" value={formik.values.cep} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} />} error={formik.touched.cep && formik.errors.cep} />
                <ProfileField label="Bairro" value={<TextField id="neighborhood" name="bairro" variant="outlined" value={formik.values.bairro} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} />} error={formik.touched.bairro && formik.errors.bairro} />
                <ProfileField label="Logradouro" value={<TextField id="street" name="logradouro" variant="outlined" value={formik.values.logradouro} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} />} error={formik.touched.logradouro && formik.errors.logradouro} />
                <ProfileField label="Número" value={<TextField id="number" name="numero" variant="outlined" value={formik.values.numero} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} />} error={formik.touched.numero && formik.errors.numero} />
                <ProfileField label="Bloco" value={<TextField id="block" name="bloco" variant="outlined" value={formik.values.bloco} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} />} error={formik.touched.bloco && formik.errors.bloco} />
                <div className="text-center">
                    <Button onClick={handleSave} variant="contained" style={{ height: '50px', backgroundColor: '#3fbabf', borderRadius: '8px', width: '200px' }}>Salvar alterações</Button>
                </div>
                <Dialog open={openDialog} onClose={handleCloseDialog}>
                    <DialogTitle>Confirmação</DialogTitle>
                    <DialogContent>
                        <Typography variant="body1">Tem certeza de que deseja salvar as alterações?</Typography>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleCloseDialog} color="primary">Cancelar</Button>
                        <Button onClick={formik.handleSubmit} color="primary">Salvar</Button>
                    </DialogActions>
                </Dialog>
            </form>
        </div>
    );
};

export default ProfileEdit;
