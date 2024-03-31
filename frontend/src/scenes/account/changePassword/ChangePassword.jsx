import React, { useState } from 'react';
import { Typography, TextField, Button, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';
import { useTheme } from '@mui/material/styles';
import useMediaQuery from '@mui/material/useMediaQuery';
import { Formik, Field, Form } from 'formik';
import * as Yup from 'yup';

const ChangePassword = () => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
    const [openDialog, setOpenDialog] = useState(false);

    const validationSchema = Yup.object().shape({
        currentPassword: Yup.string().required('Senha atual é obrigatória'),
        newPassword: Yup.string().required('Nova senha é obrigatória').min(5, 'A nova senha deve ter no mínimo 5 caracteres'),
        confirmNewPassword: Yup.string().oneOf([Yup.ref('newPassword'), null], 'As senhas devem corresponder')
    });

    const handleCloseDialog = () => {
        setOpenDialog(false);
    };

    return (
        <div style={{ overflowX: 'auto' }}>
            <Typography variant="h5" gutterBottom style={{ color: '#29686b', marginBottom: '20px', marginTop: '28px' }}>Alterar a senha</Typography>
            <Formik
                initialValues={{ currentPassword: '', newPassword: '', confirmNewPassword: '' }}
                validationSchema={validationSchema}
                onSubmit={(values, { setSubmitting }) => {
                    // lógica de alteração de senha
                    console.log(values);
                    setOpenDialog(true); 
                    setSubmitting(false);
                }}
            >
                {({ isSubmitting }) => (
                    <Form>
                        <ProfileField name="currentPassword" label="Senha atual" isMobile={isMobile} />
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
                    <Button onClick={handleCloseDialog} color="primary">Confirmar</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
};

const ProfileField = ({ name, label, isMobile }) => {
    return (
        <div style={{ display: 'flex', flexDirection: isMobile ? 'column' : 'row', alignItems: 'center', marginBottom: '2em' }}>
            <div style={{ flexBasis: isMobile ? '100%' : '30%', color: 'rgba(1, 41, 112, 0.6)', fontSize: '1.25em' }}>{label}</div>
            <div style={{ flexBasis: '70%' }}>
                <Field name={name}>
                    {({ field, form }) => (
                        <TextField
                            {...field}
                            type="password"
                            variant="outlined"
                            style={{ width: '100%' }}
                            error={form.touched[name] && Boolean(form.errors[name])}
                            helperText={form.touched[name] && form.errors[name]}
                        />
                    )}
                </Field>
            </div>
        </div>
    );
};

export default ChangePassword;
