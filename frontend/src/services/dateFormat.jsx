export function formatarData_yyyy_MM_dd(dataString) {
    const [dia, mes, ano] = dataString.split('/');
    
    const data = new Date(`${ano}-${mes}-${dia}`);
    
    const anoFormatado = data.getFullYear();
    const mesFormatado = (data.getMonth() + 1).toString().padStart(2, '0');
    const diaFormatado = data.getDate().toString().padStart(2, '0');
    
    const dataFormatada = `${anoFormatado}-${mesFormatado}-${diaFormatado}`;
    
    return dataFormatada;
}

export function formatarData_dd_MM_yyyy(dataString) {
    if (dataString) {
        const [ano, mes, dia] = dataString.split('-');
        
        const data = new Date(`${ano}-${mes}-${dia}`);
        
        const anoFormatado = data.getFullYear();
        const mesFormatado = (data.getMonth() + 1).toString().padStart(2, '0');
        const diaFormatado = data.getDate().toString().padStart(2, '0');
        
        const dataFormatada = `${diaFormatado}/${mesFormatado}/${anoFormatado}`;
        return dataFormatada;
    }
}
