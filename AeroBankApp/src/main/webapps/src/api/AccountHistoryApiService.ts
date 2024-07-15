
export type NotificationType = {
    [acctID: string] : any[];
};

export interface FetchAccountsAndNotificationsResult {
    accounts: any;
    selectedAccount: any;
    newNotificationsByAccount: NotificationType;
}

export async function fetchAccountsAndNotifications(username: string, initialId: string) : Promise<FetchAccountsAndNotificationsResult | undefined> {
    try{
        const axios = require('axios');
        const accountsResponse = await axios.get(`http://localhost:8080/AeroBankApp/api/accounts/data`, {
            params: {
                user: username
            }
        }); // Prepare and execute all notifications fetch operations

        const accounts = accountsResponse.data;
        let selectedAccount = null;
        if (initialId != null) {
            selectedAccount = accounts.find((account: {acctID: number;}) => account.acctID === parseInt(initialId));
        }

        const notificationPromises = accounts.map((account: { acctID: any; }) =>
            axios.get(`http://localhost:8080/AeroBankApp/api/accounts/notifications/${account.acctID}`)
                .catch((error: any) => {
                    console.error(`Failed to fetch notifications for account ID ${account.acctID}:`, error);
                    return { error };
                })
        );

        // Resolve all notification promises
        const notificationResponses = await Promise.allSettled(notificationPromises);

        // Initialize notifications map from resolved promises
        const newNotificationsByAccount: NotificationType = {};
        notificationResponses.forEach((result, index) => {
            if (result.status === 'fulfilled' && Array.isArray(result.value.data)) {
                newNotificationsByAccount[accounts[index].acctID] = result.value.data;
            } else {
                newNotificationsByAccount[accounts[index].acctID] = [];
            }
        });

        return { accounts, selectedAccount, newNotificationsByAccount };
    } catch (error) {
        console.error('Error fetching account data or notifications:', error);
        // return partial results or throw an error, depending on your error handling strategy
        throw error;
    }
}