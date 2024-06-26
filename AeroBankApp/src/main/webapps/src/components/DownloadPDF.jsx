function DownLoadPDF(){
    const handleDownloadPDF = async () => {
        try {
            const response = await fetch('http://localhost:8080/AeroBankApp/api/generate-pdf');
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const blob = await response.blob();
            const downloadUrl = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = downloadUrl;\\
            link.setAttribute('download', 'generated_report.pdf'); // Define the download file name
            document.body.appendChild(link);
            link.click();
            link.parentNode.removeChild(link);
        } catch (error) {
            console.error('Failed to download the file:', error);
        }
    };

    return (
        <button onClick={handleDownloadPDF}>Download PDF</button>
    );
}