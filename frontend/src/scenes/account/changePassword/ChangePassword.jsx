import React, { useState } from 'react';
import { Typography, TextField, Button, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';
import { useTheme } from '@mui/material/styles';
import useMediaQuery from '@mui/material/useMediaQuery';
import { Formik, Field, Form } from 'formik';
import * as Yup from 'yup';
import api from '../../../services/api';

const ChangePassword = () => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const [openDialog, setOpenDialog] = useState(false);
    const [form, setForm] = useState();
    const [serverError, setServerError] = useState(null);
    const [successDialogOpen, setSuccessDialogOpen] = useState(false);

    const validationSchema = Yup.object().shape({
        currentPassword: Yup.string().required('Senha atual é obrigatória'),
        newPassword: Yup.string().required('Nova senha é obrigatória').min(5, 'A nova senha deve ter no mínimo 5 caracteres'),
        confirmNewPassword: Yup.string().oneOf([Yup.ref('newPassword'), null], 'As senhas devem corresponder')
    });

    const handleCloseDialog = () => {
        setOpenDialog(false);
        setSuccessDialogOpen(false); 
    };

    const handleConfirmDialog = async () => {
        try {
            await api.put("/usuario/alterarsenha", {
                currentPassword: form.currentPassword,
                newPassword: form.newPassword,
                confirmNewPassword: form.confirmNewPassword
            });
            setServerError(null);
            setOpenDialog(false);
            setSuccessDialogOpen(true);
        } catch  (error) {
            if (error.response && error.response.status === 400) {
                setServerError("A senha atual não é válida");
            } else {
                throw new Error("Erro ao atualizar paciente: " + error);
            }
        }

        setOpenDialog(false);
    };

    return (
        <div style={{ overflowX: 'auto' }}>
            <Typography variant="h5" gutterBottom style={{ color: '#29686b', marginBottom: '20px', marginTop: '28px' }}>Alterar a senha</Typography>
            <Formik
                initialValues={{ currentPassword: '', newPassword: '', confirmNewPassword: '' }}
                validationSchema={validationSchema}
                onSubmit={(values, { setSubmitting }) => {
                    console.log(values);
                    setForm(values)
                    setOpenDialog(true); 
                    setSubmitting(false);
                }}
            >
                {({ isSubmitting, errors }) => (
                    <Form>
                        <ProfileField name="currentPassword" label="Senha atual" isMobile={isMobile} serverError={serverError} />
                        <ProfileField name="newPassword" label="Nova Senha" isMobile={isMobile} />
                        <ProfileField name="confirmNewPassword" label="Confirmar Nova Senha" isMobile={isMobile} />
                        <div className="text-center">
                            <Button type="submit" variant="contained" color="primary" style={{ height: '50px', backgroundColor: '#3fbabf', borderRadius: '8px', width: '200px' }} disabled={isSubmitting}>Alterar a senha</Button>
                        </div>
                    </Form>
                )}
            </Formik>
            <Dialog open={openDialog} onClose={handleCloseDialog}>
                <DialogTitle>Confirmação</DialogTitle>
                <DialogContent>
                    <Typography variant="body1">Tem certeza de que deseja alterar a senha?</Typography>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDialog} color="primary">Cancelar</Button>
                    <Button onClick={handleConfirmDialog} color="primary">Confirmar</Button>
                </DialogActions>
            </Dialog>
            {/* Dialog de sucesso */}
            <Dialog open={successDialogOpen} onClose={handleCloseDialog}>
                <DialogTitle>Sucesso</DialogTitle>
                <DialogContent>
                    <Typography variant="body1">Senha alterada com sucesso!</Typography>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDialog} color="primary">Fechar</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
};

const ProfileField = ({ name, label, isMobile, serverError }) => {
    return (
        <div style={{ display: 'flex', flexDirection: isMobile ? 'column' : 'row', alignItems: 'center', marginBottom: '2em' }}>
            <div style={{ flexBasis: isMobile ? '100%' : '30%', color: 'rgba(1, 41, 112, 0.6)', fontSize: '1.25em' }}>{label}</div>
            <div style={{ flexBasis: '70%' }}>
                <Field name={name}>
                    {({ field, form }) => (
                        <div>
                            <TextField
                                {...field}
                                type="password"
                                variant="outlined"
                                style={{ width: '100%' }}
                                error={(form.touched[name] && Boolean(form.errors[name])) || (serverError && name === "currentPassword")} // Adicionando condição para erro de senha atual
                                helperText={(form.touched[name] && form.errors[name]) || (serverError && name === "currentPassword" && serverError)} // Exibindo mensagem de erro de senha atual
                                FormHelperTextProps={{ style: { marginLeft: 0 } }} 
                            />
                        </div>
                    )}
                </Field>
            </div>
        </div>
    );
};

export default ChangePassword;