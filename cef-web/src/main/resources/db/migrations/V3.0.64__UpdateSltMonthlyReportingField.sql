UPDATE slt_properties SET
  "label" = 'Monthly Reporting',
  "description" = 'Enabling allows certifiers and preparers to enter Throughput, '
    'Fuel, and Emissions values on a monthly basis.'
WHERE "name" = 'slt-feature.monthly-fuel-reporting.enabled';
