# Get_data
import pandas as pd
import numpy as np

from functions import create_client

PROJECT_ID = 'brum-dev-b72f'
SA_KEY_NAME = 'service-account-key'
DATASET = 'Kaggle_test_data'
source_table = 'Fossils'

# Create BigQuery client
bq_client = create_client(PROJECT_ID, SA_KEY_NAME)

# Read data from BigQuery
sql_src_qry = f"SELECT * FROM `{PROJECT_ID}.{DATASET}.{source_table}`"

df = bq_client.query(sql_src_qry).to_dataframe()
df.replace({np.nan: None}, inplace = True)

print(df.head())
print(df.info())