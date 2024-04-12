import React, { useState, useEffect } from 'react';
import { Typography, TextField, Button, RadioGroup, Radio, FormControlLabel, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';
import { useTheme } from '@mui/material/styles';
import useMediaQuery from '@mui/material/useMediaQuery';
import minhaImagem from '../../../assets/nulo.jpg';
import nuloImg from '../../../assets/nulo.jpg'; 
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import DeleteIcon from '@mui/icons-material/Delete';
import { useFormik } from 'formik';
import InputMask from 'react-input-mask';
import axios from 'axios';
import { isValid, isBefore, parse } from 'date-fns';
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
    const [successDialogOpen, setSuccessDialogOpen] = useState(false);
    const [image, setImage] = useState(minhaImagem);
    const [cepError, setCepError] = useState('');

    const handleSave = () => {
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
    };

    const handleOpenSuccessDialog = () => {
        setSuccessDialogOpen(true);
    };

    const handleCloseSuccessDialog = () => {
        setSuccessDialogOpen(false);
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

    const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];


    const handleImageUpload = async (event) => {
        const file = event.target.files[0];

        // Verificar se o tipo de arquivo é permitido
        if (!allowedTypes.includes(file.type)) {
            console.error('Tipo de arquivo não suportado');
            return;
        }

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
        nome: Yup.string()
            .matches(/^[a-zA-Z\s]*$/, 'O nome deve conter apenas letras e espaços')
            .min(2, 'O nome deve ter pelo menos 2 caracteres')
            .required('Campo obrigatório'),
        sobrenome: Yup.string()
            .matches(/^[a-zA-Z\s]*$/, 'O sobrenome deve conter apenas letras e espaços')
            .min(2, 'O sobrenome deve ter pelo menos 2 caracteres')
            .required('Campo obrigatório'),
        email: Yup.string().email('Formato de e-mail inválido').required('Campo obrigatório'),
        genero: Yup.string().required('Campo obrigatório'),
        cpf: Yup.string().matches(/^\d{3}\.\d{3}\.\d{3}\-\d{2}$/, 'Formato de CPF inválido').required('Campo obrigatório'),
        telefone: Yup.string()
            .required('Campo obrigatório')
            .matches(/^\(\d{2}\) \d{5}-\d{4}$/, 'Formato de telefone inválido. Use (99) 99999-9999'),
        dataDeNascimento: Yup.string()
            .required('Campo obrigatório')
            .test('valid-date', 'Data de nascimento inválida', (value) => {
                const parsedDate = parse(value, 'dd/MM/yyyy', new Date());
                const today = new Date();
                const minDate = new Date(1900, 0, 1);
                return isValid(parsedDate) && isBefore(parsedDate, today) && isBefore(minDate, parsedDate);
            }),
        cep: Yup.string()
            .matches(/^\d{5}-\d{3}$/, 'Formato de CEP inválido')
            .required('Campo obrigatório'),
        bairro: Yup.string().required('Campo obrigatório'),
        logradouro: Yup.string().required('Campo obrigatório'),
        numero: Yup.number().required('Campo obrigatório'),
        bloco: Yup.string(),
    });

    const formik = useFormik({
        initialValues: profileData,
        validationSchema: validationSchema,
        onSubmit: async (values) => {
            onSave(values);
            handleCloseDialog();

            try {
                await atualizar(values);
                handleOpenSuccessDialog();
            } catch (error) {
                console.error("Erro ao atualizar usuário: ", error);
            }
        },
    });

    const atualizar = async (values) => {
        try {
            await api.put("/usuario/alterarusuario", {
                id: values.id,
                nome: values.nome,
                sobrenome: values.sobrenome,
                dataDeNascimento: formatarData_yyyy_MM_dd(values.dataDeNascimento),
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

    const handleCEPChange = async (event) => {
        const cep = event.target.value;

        if (!/^\d{5}-\d{3}$/.test(cep)) {
            // Se o formato do CEP for inválido, limpe o erro
            setCepError('');
            formik.setFieldValue('logradouro', '');
            formik.setFieldValue('bairro', '');
            return;
        }

        try {
            const response = await axios.get(`https://viacep.com.br/ws/${cep}/json/`);
            const { logradouro, bairro } = response.data;

            if (!logradouro || !bairro) {
                // Se o CEP não for encontrado, defina o erro
                setCepError('CEP não encontrado');
                formik.setFieldValue('logradouro', '');
                formik.setFieldValue('bairro', '');
                return;
            }

            // Se o CEP for válido, limpe o erro e atualize os campos logradouro e bairro
            setCepError('');
            formik.setFieldValue('logradouro', logradouro);
            formik.setFieldValue('bairro', bairro);
        } catch (error) {
            // Em caso de erro na requisição, defina uma mensagem genérica
            setCepError('Erro ao buscar CEP');
            formik.setFieldValue('logradouro', '');
            formik.setFieldValue('bairro', '');
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
                <ProfileField label="Nome" value={<TextField id="name" name="nome" variant="outlined" value={formik.values.nome.replace(/\b\w/g, (c) => c.toUpperCase())} onChange={(event) => /^[a-zA-Z\s]*$/.test(event.target.value) && formik.handleChange(event)} style={{ width: '100%', textAlign: 'center' }} />} error={formik.touched.nome && formik.errors.nome} />

                <ProfileField label="Sobrenome" value={<TextField id="surname" name="sobrenome" variant="outlined" value={formik.values.sobrenome.replace(/\b\w/g, (c) => c.toUpperCase())} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} />} error={formik.touched.sobrenome && formik.errors.sobrenome} />

                <ProfileField label="E-mail" value={<TextField id="email" name="email" variant="outlined" value={formik.values.email} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} />} error={formik.touched.email && formik.errors.email} />
                <ProfileField label="Gênero" value={
                    <RadioGroup name="genero" value={formik.values.genero} onChange={formik.handleChange} style={{ display: 'flex', flexDirection: 'row' }}>
                        <FormControlLabel value="Masculino" control={<Radio style={{ color: '#3fbabf' }} />} label="Masculino" />
                        <FormControlLabel value="Feminino" control={<Radio style={{ color: '#3fbabf' }} />} label="Feminino" />
                        <FormControlLabel value="Outro" control={<Radio style={{ color: '#3fbabf' }} />} label="Outro" />
                    </RadioGroup>
                } error={formik.touched.genero && formik.errors.genero} />
                <ProfileField label="CPF" value={<TextField id="cpf" name="cpf" variant="outlined" value={formik.values.cpf} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} disabled />} error={formik.touched.cpf && formik.errors.cpf} />
                <ProfileField label="Telefone" value={<InputMask mask="(99) 99999-9999" maskChar={null} value={formik.values.telefone} onChange={formik.handleChange}>{(inputProps) => (<TextField {...inputProps} id="phone" name="telefone" variant="outlined" style={{ width: '100%', textAlign: 'center' }} />)}</InputMask>} error={formik.touched.telefone && formik.errors.telefone} />
                <ProfileField label="Data de Nascimento" value={<InputMask mask="99/99/9999" maskChar="_" value={formik.values.dataDeNascimento} onChange={formik.handleChange}>{(inputProps) => (<TextField {...inputProps} id="birthdate" name="dataDeNascimento" variant="outlined" style={{ width: '100%', textAlign: 'center' }} />)}</InputMask>} error={formik.touched.dataDeNascimento && formik.errors.dataDeNascimento} />
                <ProfileField label="CEP" value={<InputMask mask="99999-999" maskChar="_" value={formik.values.cep} onChange={(event) => { formik.handleChange(event); handleCEPChange(event); }}>{(inputProps) => (<TextField {...inputProps} id="cep" name="cep" variant="outlined" style={{ width: '100%', textAlign: 'center' }} error={!!cepError} helperText={cepError} />)}</InputMask>} error={formik.touched.cep && formik.errors.cep} />
                <ProfileField label="Bairro" value={<TextField id="neighborhood" name="bairro" variant="outlined" value={formik.values.bairro} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} />} error={formik.touched.bairro && formik.errors.bairro} />
                <ProfileField label="Logradouro" value={<TextField id="street" name="logradouro" variant="outlined" value={formik.values.logradouro} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} />} error={formik.touched.logradouro && formik.errors.logradouro} />
                <ProfileField label="Número" value={<TextField id="number" name="numero" variant="outlined" value={formik.values.numero} onChange={formik.handleChange} style={{ width: '100%', textAlign: 'center' }} type="number" />} error={formik.touched.numero && formik.errors.numero} />
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
                 <Dialog open={successDialogOpen} onClose={handleCloseSuccessDialog}>
                    <DialogTitle>Sucesso</DialogTitle>
                    <DialogContent>
                        <Typography variant="body1">Alterações salvas com sucesso!</Typography>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleCloseSuccessDialog} color="primary">Fechar</Button>
                    </DialogActions>
                </Dialog>
                
            </form>
        </div>
    );
};

export default ProfileEdit;
