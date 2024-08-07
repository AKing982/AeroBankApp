import { PDFViewer, Document, Page, Text, View, StyleSheet } from '@react-pdf/renderer';

function ReportPDF({reportContent, reportName}){

    const styles = StyleSheet.create({
        page: {
            flexDirection: 'column',
            backgroundColor: '#E4E4E4'
        },
        section: {
            margin: 10,
            padding: 10,
            flexGrow: 1
        }
    });

    return (
        <Document>
            <Page size="A4" style={styles.page}>
                <View style={styles.section}>
                    <Text style={{ fontSize: 24 }}>{reportName}</Text>
                    <Text>{reportContent}</Text>
                </View>
            </Page>
        </Document>
    );
}

export default ReportPDF;