import { Routes } from '@angular/router';

export const routes: Routes =
    [
        {
            path: 'burrow',
            loadComponent: () => import('./Pages/burrow-page/burrow-page.component').then(m => m.default)
        },
        {
            path: 'tunnel',
            loadComponent: () => import('./Pages/tunnel/tunnel').then(m => m.default),
        },
        {
            path: '**',
            redirectTo: 'burrow',
        }
    ];
